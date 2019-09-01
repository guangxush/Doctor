## 数据采集的背景
用户每天通过不同的终端设备在系统A上做出一系列的操作，比如在新闻系统上的看一些文章，进行收藏或者点赞、评论等等，会触发A系统下不同的微服务，如abc等，如何记录好用户的行为信息，并将其保存下来用于分析用户的行为偏好，需要结合不同的数据采集策略。
![业务系统](https://upload-images.jianshu.io/upload_images/7632302-e88faa5e2ac2d9c5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 数据采集的几种方式
### 方式一：读备份库
为了不影响业务系统的正常运行，可以采用读备份库的数据，这样能够及时获取数据进行一些分析工作，但是有些从业务也会读取备份数据库，还需要考虑一致性和可用性问题。

![数据库备份](https://upload-images.jianshu.io/upload_images/7632302-449511351e9ac077.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 方式一： 埋点(pingback)
可以在前端APP上记录用户点击，滑动速度，停留时间，进入的时间段，最后看的新闻等等信息，这些可以通过网络传输将埋点信息记录下来，用于数据分析。但是这种方式有可能会对业务系统代码具有一定的侵入性，同时工作量也比较大，存在一定的安全隐患。
![埋点](https://upload-images.jianshu.io/upload_images/7632302-4a4009a10a00fda0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

后端采集数据的service
```
/**
     * 埋点接收数据
     * @param pingBack
     * @return
     */
    @RequestMapping(path = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Boolean> insert(@RequestBody PingBack pingBack) {
        Boolean result = patientService.savePingBack(pingBack);
        return new ApiResponse<Boolean>().success(result);
    }
```
已有的业务系统可以给数据采集系统发送数据
```
/**
     * pingback方式插入
     * @param patient
     * @return
     */
    @RequestMapping(path = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Boolean> insert(@RequestBody Patient patient) {
        try{
            Boolean result = patientService.savePatient(patient);
            return new ApiResponse<Boolean>().success(result);
        }catch (InternalError error){
            log.error("insert error");
        }finally {
            pingBackService.jsonRequest(url+"insert", patient);
        }
        return null;
    }
```
### 方式三： 发送消息的方式
上述埋点的方式在业务系统繁忙的情况下，会对数据采集系统产生大量的请求，如果数据处理不及时会把数据采集服务打垮，同时为了解耦，这里可以引入消息中间件，如果对时效性要求较高，可以采用推模式对数据采集系统进行推送，如果时效性不是很高，可以采用定时任务拉取数据，再进行分析。
同时可以多个系统订阅消息中间件中不同Topic的数据，可以对数据进行重用，后端多个数据分析系统之间互不影响，减轻了从业务系统采集多份数据的压力。
![引入消息中间件](https://upload-images.jianshu.io/upload_images/7632302-c0670d854026c305.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
数据采集Service
 ```
/**
     * 消息中间件的方式更新
     * @param patient
     * @return
     */
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Boolean> update(@RequestBody Patient patient) {
        try{
            Boolean result = patientService.updatePatient(patient);
            return new ApiResponse<Boolean>().success(result);
        }catch (InternalError error){
            log.error("update error");
        }finally {
            sendMessageService.send(patient);
        }
        return null;
    }
```

中间件发送数据实现（以kafka为例）
```
@Service
@Slf4j
public class SendMessageService {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("topic")
    private String topic;

    private ObjectMapper om = new ObjectMapper();

    public boolean send(Object object){
        String objectJson = "";
        try {
            objectJson = new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            log.error("can't trans the {} object to json string!", object);
            return false;
        }
        try{
            String result = kafkaTemplate.send("mysql-kafka-patient", objectJson).get().toString();
            if(result!=null){
                return true;
            }

        }catch (Exception e){
            return false;
        }
        return false;
    }
}
```
中间件拉取数据：

```
@KafkaListener(id = "forward", topics = "mysql-kafka-patient")
    public String forward(String data) {
        log.info("mysql-kafka-patient "+data);
        JSONObject jsonObject1 = JSONObject.parseObject(data);
        Message message = (Message) JSONObject.toJavaObject(jsonObject1,Message.class);
        messageService.updateMessage(message);
        return data;
    }
```
### 方式四：读取MySQL中的binlog
MySQL会把数据的变更（插入和更新）保存在binlog中，需要在my.ini中配置开启，因此采用kafka订阅binlog，会将DB中需要的字段抓取出来，保存在备份库中，进行数据分析，工作量较小，安全稳定。
```text
name=mysql-b-source-pingBack
connector.class=io.confluent.connect.jdbc.JdbcSourceConnector
tasks.max=1
connection.url=jdbc:mysql://localhost:3306/test?user=root&password=root
# timestamp+incrementing 时间戳自增混合模式
mode=timestamp+incrementing
# 自增字段  id
timestamp.column.name=commenttime
incrementing.column.name=id
# 白名单表  pingBack
table.whitelist=pingBack
# topic前缀   mysql-kafka-
topic.prefix=mysql-kafka-
```
具体使用可以参考：[官方文档](https://docs.confluent.io/2.0.0/connect/connect-jdbc/docs/index.html#examples)


## 分析对比
|数据采集方式 |优点        |缺点      |
|:-----------:|:----------:|:---------:|
|埋点(pingback)          |很细致的将前端用户操作记录下来，能够感知到DB存储之外的用户信息，时效性高 |工作量大，可能对业务代码有侵入性；当业务量大的时候，数据抓取服务也需要承载一定的压力，对数据不方便统计和聚合 |
|主库写备库读         |及时感知备库中的信息 ，数据一致性强|可能存在大量不需要进行分析的字段，对业务性能有影响 |
|埋点+消息中间件  |有效的解决业务量大时对数据存取性能的要求，根据数据抓取服务的需求可以拉也可以推，解耦业务代码 |可能会丢失数据，降低了时效性 |
|订阅binlog |工作量小，可以离线感知数据的变化，对数据变更进行统计分析 |不能感知除DB数据变更之外的用户行为 |

因此对数据的抓取可以多种方式结合，具体还是要根据后端数据分析任务对数据的时效性、需求和性能综合考虑。
## 源码分享
Demo1：患者服务（patient）会产生不同的病例记录，然后doctor服务端采用kafka/pingback/binlog三种不同方式感知患者的患病信息，对数据进行有效的采集。
[源码参考](https://github.com/guangxush/Doctor)
Demo2: 一个数据采集的微服务，用户发送请求到微服务系统中，系统通过日志的形式将请求信息持久化到文件中。
[源码参考](https://github.com/guangxush/HelloData)


