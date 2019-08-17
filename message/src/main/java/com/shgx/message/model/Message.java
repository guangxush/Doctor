package com.shgx.message.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: guangxush
 * @create: 2019/08/17
 * 患者信息记录
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class Message {
    /**
     * 自增id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 患病记录
     */
    @Column(name = "content")
    private String content;

    /**
     * 记录时间
     */
    @Column(name = "commenttime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commenttime;
}
