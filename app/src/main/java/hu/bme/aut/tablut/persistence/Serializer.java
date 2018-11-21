package hu.bme.aut.tablut.persistence;
import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

    public static Object load(Context context, String fileName) {
        Object o = null;
        File file = new File(context.getFilesDir(), fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream is = new ObjectInputStream(fis);
            o = is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    public static void save(Context context, Object object, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir(), fileName));
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(object);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
