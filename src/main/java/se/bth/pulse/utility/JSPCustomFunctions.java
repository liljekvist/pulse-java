package se.bth.pulse.utility;

import java.util.List;
import se.bth.pulse.entity.User;

public class JSPCustomFunctions {

  public static boolean contains(List list, Object o) {
    return list.contains(o);
  }

  public static boolean containsUser(List<User> list, Integer o) {
    return list.stream().anyMatch(user -> user.getId().equals(o));
  }

}
