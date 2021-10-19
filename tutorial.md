ParamCheck `<ver.20210915>`版本参数校验使用说明
--

* **_主要调整：_**  
1、移除校验中的元素递归校验  
2、增加使用注解对对象字段的校验  
3、增加分组校验  
4、增加一些其它的校验规则(注解)  
5、FieldInspect 类调整  
6、错误字段信息结果调整  `List<String>` 改为 `Map<String, Set<String>>`，记录具体字段和具体的错误信息  
7、增加`@EnableParamCheck` 用于表示是否启用参数校验功能


---
* 注解使用时的可选参数说明：  

    * `Class[] classes()` &emsp; 校验的类
    * `String errorCode()` &emsp; 错误码【默认为 400154】
    * `int httpCode()` &emsp; HTTP状态码【默认为 200】
    * `int min()` &emsp; 最大元素个数&nbsp; /&nbsp; 最大字符长度
    * `int max()` &emsp; 最小元素个数&nbsp; /&nbsp; 最小字符长度
    * `boolean nullable()` &emsp; 可为空
    * `String msg()` &emsp; 错误提示语  
    * `String[] groups()` &emsp; 分组校验信息【未指定分组时为必校验字段】
    * `String format()` &emsp; 日期格式【未指定时默认为13位时间戳】
    * `int decimalPlaces()` &emsp; 限制小数位位数
    
   
---  

*  **_新增的注解及其校验规则：_**  
  
   ######  
    
      * @InnerObj   
    
        > 用来标识内部对象类型  
          标识多重嵌套结构数据    
          使用方法：   @InnerObj(xxx.class)
        
   ###### 备注：以下所有注解都可通过 nullable 指定是否进行非空判断；非空判断时会根据参数类型进行不同的判断
       
     * @ElementLength 
        
       > 1、字段值类型校验须为 List\<String> 或 String[]  
         2、元素字符的长度范围校验  
 
     * @Future
       > int value() 时间差值(默认为当天0点，负数时日期往前推，整数往后)  
       
       > 1、时间晚于当天0点   
         2、时间格式校验

     * @Length
       > 1、数组元素个数范围校验  
         2、字符串长度范围校验
         
     * @MaxElementLength
       > 1、字段值类型校验须为 List\<String> 或 String[]  
         2、元素字符的长度范围校验  
        
     * @MaxLength
       > 1、数组元素最大个数校验  
         2、字符串最大长度校验
    
     * @MaxSize
       > 1、数组元素最大个数校验
      
     * @MaxValue
       > 1、数字类型校验  
         2、最大值校验
     
     * @MinLength
       > 1、数组元素最小个数校验  
         2、字符串最小长度校验
         
     * @MinValue
       > 1、数字类型校验   
         2、最小值校验
        
     * @NonNull
       > 1、非空校验 

     * @Past
        > int value() 时间差值(默认为当天0点，负数时日期往前推，整数往后)
        
        > 1、时间早于当天0点  
          2、时间格式校验
          
     * @Pattern
        > 1、使用正则表达式校验

---

*  FieldInspect 类调整说明
    * 新增 `Map<String, List<String>> badFields` 字段记录字段的错误信息，key为字段名称，value为错误信息的集合
    * `JSONObject params` 为传入所有参数；
    * `ParamCheck paramCheck` 校验的入口，可以获取校验时的自定义信息
    * 通过重写`ErrorResultHandler.handler(JSONObject params, Map<String, Set<String>> badFields, ParamCheck paramCheck)` 自定义返回错误信息  
        * `Map<String, Set<String>>` 结构说明：key 具体字段名，包含对象名；
        * `Set<String>` 是错误信息
     
    
---
* 注意项
    * 接收参数的对象不包含其父类的校验字段
    * 选择合适的注解能提高校验的效率
    * 必须实现`ErrorResultHandler.handler()` 方法