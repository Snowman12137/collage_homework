/**
 * @Author: xiaowu
 * @Description:
 * @Date: 2022/10/30 0030 21:36
 * @Parms: 
 * @ReturnType: 
 */
package newpacket;

public class ArrayUtil {
    //私有化构造方法
    private ArrayUtil(){}



    //需要定义为静态的，方便调用
    public static String printArr(int[] arr){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < arr.length; i++) {
            if(i == arr.length-1){
                sb.append(arr[i]);
            }
            else{
                sb.append(arr[i]).append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }



}
