package demo;

import java.util.HashSet;

public class Friend {
  private HashSet<Friend> friends;
  private String email;

  private static final long LIMIT = 9;

  public Friend(String email) {
    this.email = email;
    this.friends = new HashSet<Friend>();
  }

  public String getEmail() {
    return email;
  }

  public HashSet<Friend> getFriends() {
    return friends;
  }

  public void addFriendship(Friend friend) {
    friends.add( friend );
    friend.getFriends().add( this );
  }

  public HashSet<Friend> withinDist(HashSet<Friend> mob, long dist) {
    HashSet<Friend> result = new HashSet<Friend>();
    result.addAll( mob );
    result.add( this );
    if (dist > 0) {
      result.addAll( friends );
      for (Friend currFriend : friends) {
        if (!mob.contains( currFriend )) {
          HashSet<Friend> fof = currFriend.withinDist( result, (dist - 1) );
          result.addAll( fof );
        }
      }
    }
    return result;
  }

  public boolean canBeConnected(Friend tgtFriend) {
    if (friends.contains( tgtFriend )) {
      return true;
    }
    HashSet<Friend> emptyMob = new HashSet<Friend>();
    for (long dist = 0; dist <= LIMIT; dist++) {
      HashSet<Friend> circle = this.withinDist( emptyMob , dist );
      if (circle.contains( tgtFriend )) {
        return true;
      }
    }
    return false;
  }

  public String toString() {
    return email;
  }

}
