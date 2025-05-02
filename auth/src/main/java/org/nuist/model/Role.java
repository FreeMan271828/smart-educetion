package org.nuist.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@TableName("roles")
public class Role {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull
    private String roleName;

}
