// 对象克隆
export function clone(obj) {
    var o;
    // 如果  他是对象object的话  , 因为null,object,array  也是'object';
    if (typeof obj === 'object') {
      
      // 如果  他是空的话
      if (obj === null) {
        o = null;
      }
      else {
    
        // 如果  他是数组arr的话
        if (obj instanceof Array) {
          o = [];
          for (var i = 0, len = obj.length; i < len; i++) {
            o.push(clone(obj[ i ]));
          }
        }
        // 如果  他是对象object的话
        else {
          o = {};
          for (var j in obj) {
            o[ j ] = clone(obj[ j ]);
          }
        }
        
      }
    }
    else {
      o = obj;
    }
    return o;
  };
  //判断obj是否有空值---校验  arr为不检验的参数
  export function isEmpty(obj,arr){
    if(!arr){arr =[]}
    console.log(arr,arr.length)
    let flag = true;
    for(var key in obj){　　//遍历对象的所有属性，包括原型链上的所有属性
      if(obj.hasOwnProperty(key)){ //判断是否是对象自身的属性，而不包含继承自原型链上的属性
        if(arr.indexOf(key)!= -1){ 
        }else if(!obj[key]){
          flag=false;
        }
      }
    }
    return flag
  };

  //obj为初始化的对象 arr为obj内不必初始化的值
  export function initObj(obj,arr){
    let flag = true;
    for(var key in obj){　
      if(obj.hasOwnProperty(key)){ 
        if(arr && arr.indexOf(key)!= -1){ 
        }else{
          obj[key] = '';
        }
        
      }
    }
    return obj
  };

//  newObj为待复制的对象   copyobj为给newobj赋值的obj
  export function objCopy(newObj,copyObj){
    
    for(var key in copyObj){　　//遍历对象的所有属性，包括原型链上的所有属性
      if(copyObj.hasOwnProperty(key)){ //判断是否是对象自身的属性，而不包含继承自原型链上的属性
          for(var newkey in newObj){　　
            if(newObj.hasOwnProperty(newkey)){ 
               if(key == newkey){
                newObj[newkey] = copyObj[key]
               }
            }
        }
      }
    }
    return newObj
  };
