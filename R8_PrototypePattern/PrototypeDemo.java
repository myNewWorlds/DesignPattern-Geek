import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 原型模式的 深拷贝与浅拷贝
 */
public class PrototypeDemo {
    public static void main(String[] args) throws Exception {
        HashMap<String, User> oriUserMap = new HashMap<>();
        for (int i = 1; i < 11; i++) {
            String id = String.valueOf(i);
            oriUserMap.put(id, new User(id, id));
        }

        //浅拷贝
        System.out.println(oriUserMap.get("3").getName()); //3
        HashMap<String, User> newUserMap = (HashMap<String, User>) oriUserMap.clone();
        newUserMap.get("3").setName("新3");
        System.out.println(oriUserMap.get("3").getName()); //新3
        oriUserMap.get("3").setName("3");

        //深拷贝1
        System.out.println(oriUserMap.get("3").getName()); //3
        HashMap<String, User> newUserMap_2 = new HashMap<>();
        for (Map.Entry<String, User> entry : oriUserMap.entrySet()) {
            String key = entry.getKey();
            User user = new User(entry.getValue().getId(), entry.getValue().getName());
            newUserMap_2.put(key, user);
        }
        newUserMap_2.get("3").setName("新3");
        System.out.println(oriUserMap.get("3").getName()); //3

        //深拷贝2  序列化，再反序列化
        System.out.println(oriUserMap.get("3").getName()); //3
        //一个输出流，可以将数据写入到内存中的字节数组中
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        //对象输出流，用于将对象写入到输出流
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(oriUserMap);

        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInput oi = new ObjectInputStream(bi);
        HashMap<String, User> newUserMap_3 = (HashMap<String, User>) oi.readObject();
        newUserMap_3.get("3").setName("新3");
        System.out.println(oriUserMap.get("3").getName()); //3
    }
}
