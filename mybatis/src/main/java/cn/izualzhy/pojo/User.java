package cn.izualzhy.pojo;

import org.apache.ibatis.type.Alias;

@Alias(value = "user")// MyBatis指定别名
public class User  {
   private Long id = null;
   private String userName = null;
   // 性别枚举，这里需要使用typeHandler进行转换
   private SexEnum sex = null;
   private String note = null;
   public User() {
   }
}