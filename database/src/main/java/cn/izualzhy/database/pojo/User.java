package cn.izualzhy.database.pojo;

import cn.izualzhy.database.dic.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("user")
@Data
@AllArgsConstructor
public class User {
   private Long id = null;
   private String userName = null;
   private SexEnum sex = null;// 枚举
   private String note = null;
}
