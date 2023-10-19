package se.bth.pulse.utility;

import java.util.List;
import se.bth.pulse.entity.User;

/**
 * This class defines custom functions for use in jstl tags in the jsp files.
 */
public class JspCustomFunctions {

  public static boolean contains(List list, Object o) {
    return list.contains(o);
  }

  /**
   * This function is used to find if a users it is in a list of users.
   *
   * @param list - the list to search
   * @param o    - the object to search for
   * @return boolean    - true if the list contains the object
   */
  public static boolean containsUser(List<User> list, Integer o) {
    return list.stream().anyMatch(user -> user.getId().equals(o));
  }

}
