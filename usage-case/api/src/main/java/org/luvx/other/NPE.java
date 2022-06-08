package org.luvx.other;

import static org.luvx.other.Returned.UNDEFINED;

import org.luvx.common.util.PrintUtils;
import org.luvx.other.Returned.ReturnValue;
import org.luvx.other.Returned.Undefined;

import io.vavr.API;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NPE {
    private String name;

    public Returned getName() {
        return name == null ? UNDEFINED : new ReturnValue<>(name);
    }

    private static boolean nameEquals(NPE npe, String middleName) {
        return switch (npe.getName()) {
            case Undefined ignored -> false;
            case ReturnValue rv -> rv.value().equals(middleName);
        };
    }

    public static void main(String[] args) {
        boolean l = nameEquals(new NPE("luvx"), "luvx");
        PrintUtils.println(l);
    }
}
