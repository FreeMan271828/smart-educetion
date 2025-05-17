# smart education backend

**5.16更新内容**
- 创建了聚合启动模块`app`，并移除了student和teacher模块`pom.xml`中的build插件项（使得仅有`app`模块生成正确打包的fat jar用于运行）
- 将`student`模块中，一些与鉴权相关的接口移除（包括登录、修改密码、检测用户是否存在），并设置了新的注册接口
  - 现在设计为：student和teacher都通过唯一的`username`字段与User的`username`相关联，实现一对一关系，且student和teacher中的`password`字段现在在业务中没有实际作用（数据表和数据模型仍保持现有设计）
- 在`auth`模块中创建了注册方法（会在student/teacher的注册接口最后调用，保存用户信息），设置了修改密码、检查用户存在的接口
- 将跨域设置为全域放行，方便调试

## 安全配置模块设计
> 涉及 common 和 auth 模块

- 安全配置位于每个模块的`org.nuist.config.security`中
- 在 common 中配置 JwtUtil，用于token的生成和验证
- 在 common 中设置基本安全配置
- 在 auth 中的安全配置：
  1. `AuthSecurityConfiguration`：放行所有`/api/auth/**`接口
  2. `ApiSecurityConfiguration`：集成JwtFilter，验证剩余的API请求是否验证身份（需登录后携带token访问）

**其他模块调用时的注意点**
1. 安全和用户鉴权配置，只要引入了auth模块就能够自动配置，不需要额外的安全配置；
2. 现在的行为是`/api/auth/**`都放行，剩余的`/api/**`都需要登录后访问。如果后期有变更，则还需要更改auth模块里的配置。


---
> 还有几点疑问：
> 1. 由于每个模块的顶级包名都是相同的（org.nuist），所以只要引入了对应模块，并且在启动类上加上了`scanBasePackages={"org.nuist"}`，就能够自动引入这些模块的Bean，包括配置和controller等等。这种情况下，是否只需要定义一个启动模块并启动就可以了，不用每个模块单独启动？（感觉这样相当于物理结构上分层了，逻辑结构上还是属于同一个包）
