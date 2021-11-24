ParamCheck 参数校验使用说明
***



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
    
    ######  注意：如果 @ParamCheck 中 groups 未指定则校验所有加了注解的字段  
   
---

*  **_操作符校验规则（默认非空）：_**
    ######
    * `|` 字段多选一必填
    * `~` 最大长度
    * `=` 字符长度等于  
    * `>` 字段值大于指定值
    * `<` 字段值小于指定值

---  

*  **_注解校验规则：_**  
  
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
       > 1、数组元素长度范围校验  
         2、字符串长度范围校验
         
     * @MaxElementLength
       > 1、字段值类型校验须为 List\<String> 或 String[]  
         2、元素字符的长度范围校验  
        
     * @MaxLength
       > 1、数组元素最大长度校验  
         2、字符串最大长度校验
    
     * @MaxSize
       > 1、数组元素最大个数校验
      
     * @MaxValue
       > 1、数字类型校验  
         2、最大值校验
     
     * @MinLength
       > 1、数组元素最小长度校验  
         2、字符串最小长度校验
         
     * @MinValue
       > 1、数字类型校验   
         2、最小值校验
        
     * @NonNull
       > 1、非空校验 (包含子元素)

     * @Past
        > int value() 时间差值(默认为当天0点，负数时日期往前推，整数往后)
        
        > 1、时间早于当天0点  
          2、时间格式校验
          
     * @Pattern
        > 1、使用正则表达式校验
        
     * @ValueSet
        > 1、限制取值集(int 作为String类型处理)

    

---

* 注意项
    * 接收参数的对象不包含其父类的校验字段
    * 选择合适的注解能提高校验的效率
    * 必须实现`ErrorResultHandler.handler()` 方法