package pl.kuezese.region.type;

import pl.kuezese.region.helper.StringHelper;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Flag {

    blockplace("blockplace"),
    blockbreak("blockbreak"),
    pvp("pvp"),
    use("use"),
    burn("burn"),
    fade("fade"),
    grow("grow"),
    fromto("fromto"),
    leafdecay("leafdecay"),
    explode("explode"),
    bucket("bucket"),
    mobspawning("mobspawning"),
    enderpearl("enderpearl"),
    falldamage("falldamage"),
    fire("fire");

    private final String name;

    Flag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Flag get(String name) {
        return Arrays.stream(Flag.values()).filter(flag -> flag.getName().equals(name)).findAny().orElse(null);
    }

    public static String all() {
        return StringHelper.join(Arrays.stream(values()).map(Flag::getName).collect(Collectors.toList()), ", ");
    }
}
