# smart education backend

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
> 2. 我在auth模块里定义了一个`application.yml`，用于启动dev Profile配置；但是我发现所有引用auth的模块都自动应用了这个配置。这个表现应当是不太合理的，application.yml应该是全局的，应该是要放在父级，或者是启动模块里面？
