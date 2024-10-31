package fileio;

import com.fasterxml.jackson.annotation.JsonIgnore;

public final class Coordinates {
   private int x, y;
   private boolean isEnemyPosition = false;

   public Coordinates() {
   }

   public Coordinates(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public Coordinates(int x, int y, boolean isEnemyPosition) {
      this.x = x;
      this.y = y;
      this.isEnemyPosition = isEnemyPosition;
   }

   public int getX() {
      return x;
   }

   public void setX(final int x) {
      this.x = x;
   }

   public int getY() {
      return y;
   }

   public void setY(final int y) {
      this.y = y;
   }

   @JsonIgnore
   public boolean getIsEnemyPosition() { return isEnemyPosition; }

   public void setIsEnemyPosition(boolean isEnemyPosition) { this.isEnemyPosition = isEnemyPosition; }

   @Override
   public String toString() {
      return "Coordinates{"
              + "x="
              + x
              + ", y="
              + y
              + '}';
   }
}
