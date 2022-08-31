package org.luvx.github.more.lambda;

import static com.github.phantomthief.util.MoreSuppliers.lazy;
import static org.luvx.common.util.JsonUtils.fromJson;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;

import com.github.phantomthief.util.MoreSuppliers.CloseableSupplier;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Meow {
    @Setter
    private long   id;
    private String extParams;

    private final transient CloseableSupplier<Map<String, Object>> resolved = lazy(
            () -> fromJson(extParams));

    public void setExtParams(String extParams) {
        this.extParams = extParams;
        resolved.tryClose();
    }

    public String getTitle() {
        return MapUtils.getString(resolved.get(), MeowKey.title.name());
    }

    public enum MeowKey {
        title,
        aa,
    }
}
