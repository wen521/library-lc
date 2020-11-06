package com.even.lc.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name= "user")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Username ,not null
     */
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * user password
     */
    private String password;

    /**
     *Salt for encoding
     */
    private String salt;

    /**
     * User true name
     */
    private String name;

    /**
     * User phone number
     */
    private String phone;

    /**
     * User email
     * can be null,but should be correct if exists
     */
    @Email(message = "请输入正确的邮箱")
    private String email;

    /**
     * User status
     */
    private boolean enabled;

    /**
     * Transient property for storing role owned by current user.
     *  就是在给某个javabean上需要添加个属性，但是这个属性你又不希望给存到数据库中去，
     *  仅仅是做个临时变量，用一下。不修改已经存在数据库的数据的数据结构
     */
    @Transient
    private List<AdminRole> roles;

}
