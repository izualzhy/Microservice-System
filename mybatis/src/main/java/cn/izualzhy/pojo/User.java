package cn.izualzhy.pojo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias(value = "alias_user")// MyBatis指定别名
public class User {
   private Long id = null;
   private String userName = null;
   // 性别枚举，这里需要使用 typeHandler 进行转换
   private SexEnum sex = null;
   private String note = null;

   public User() {
   }
}