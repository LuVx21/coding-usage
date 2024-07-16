package org.luvx.serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements Serializer {
    public <T> byte[] serialize(T obj) {
        Hessian2Output ho = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ho = new Hessian2Output(os);
            ho.writeObject(obj);
            ho.flush();
            return os.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("序列化失败:" + obj, ex);
        } finally {
            if (null != ho) {
                try {
                    ho.close();
                } catch (Exception _) {
                }
            }
        }
    }

    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Hessian2Input hi = null;
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
            hi = new Hessian2Input(is);
            Object o = hi.readObject();
            return clazz.cast(o);
        } catch (Exception ex) {
            throw new RuntimeException("反序列化失败", ex);
        } finally {
            if (null != hi) {
                try {
                    hi.close();
                } catch (IOException _) {
                }
            }
        }
    }

}
