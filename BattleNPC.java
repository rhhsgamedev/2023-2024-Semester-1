import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public class BattleNPC extends BattleChar {

    BattleNPC(Game sys, int x, int y, String name) {
        super(sys, x, y, name);
    }
    
    public void update() {

        if (currentFrameDelay == frameDelay) {
            // last frame
            String animName;
            if (actionAnim == null) {
                if (charSprites.get(name + animState + (this.frameIndex + 1)) == null) {
                    if (loopBackTo > 0) {
                        this.frameIndex = loopBackTo;
                    } else {
                        this.frameIndex = 0;
                    }
                } else {
                    this.frameIndex += 1;
                }
                currentFrameDelay = 0;
                BufferedImage img = charSprites.get(name + animState + this.frameIndex);
                element.changeImage(img, img.getWidth() * 2, img.getHeight() * 2);
            } else {
                if (charSprites.get(name + actionAnim + (this.frameIndex + 1)) == null) {
                    if (loopBackTo > 0) {
                        this.frameIndex = loopBackTo;
                    } else {
                        this.frameIndex = 0;
                        actionAnim = null;
                    }
                } else {
                    this.frameIndex += 1;
                    BufferedImage img = charSprites.get(name + actionAnim + String.valueOf(this.frameIndex));
                    element.changeImage(img, img.getWidth() * 2, img.getHeight() * 2);
                }
                currentFrameDelay = 0;
            }
        }

        currentFrameDelay += 1;
        element.setElementX(getX() - element.getImage().getWidth());
        element.setElementY(getY() - (element.getImage().getHeight() * 2));

        if (createAfterimages) {
            if (currentAfterimageTick == afterimageTick) {
                // create afterimage
                currentAfterimageTick = 0;
                createAfterimage();
            } else {
                currentAfterimageTick += 1;
            }
        }

        HashSet<ImageElement> temp = new HashSet<>();
        for (ImageElement after : afterimages) {
            double newTrans = after.getTransparency() - 0.02;
            if (newTrans < 0) {
                after.setTransparency(0);
                temp.add(after);
            } else {
                after.setTransparency((float) newTrans);
            }
        }
        for (ImageElement after : temp) {
            afterimages.remove(temp);
            sys.getAllScreenElements().remove(temp);
        }
    }

    public void attack() {
        if ((stun > 0)) {
            if ((actionAnim == null) || ((!actionAnim.equals("stunned") && (!actionAnim.equals("juggle")))))  {
                changeAttackAnim("stunned", 5, 0);
            }
        } else if ((actionAnim == null) || (animCancel == true)) {

        }
    }

    public void move(Tile[][] tileMap) {
        // cant move during attack

        // up, down, left, right
        boolean[] collisions = new boolean[4];

        double[] frameMovement = new double[2];
        frameMovement[0] = ((horiInput[0] + horiInput[1]) * 5 + velocity[0]);
        frameMovement[1] = velocity[1];

        velocity[1] = Math.min(10, velocity[1] + 0.3);
        if (velocity[0] != 0) {
            if (velocity[0] > 0) {
                velocity[0] -= 0.2;
                if (velocity[0] < 0) {
                    velocity[0] = 0;
                }
            } else {
                velocity[0] += 0.2;
                if (velocity[0] > 0) {
                    velocity[0] = 0;
                }
            }
        }


        // movement anims
        if (actionAnim == null) {
            if ((airTime > 10)) {
                changeMoveAnim("jump", 15, 2);
            } else if ((frameMovement[0] != 0)) {
                changeMoveAnim("walk", 5);
            } else if ((frameMovement[0] == 0)) {
                changeMoveAnim("idle", 10);
            }
        } else if (actionAnim.equals("juggle")) {
            if ((airTime == 0) && (stun == 0)) {
                velocity[1] = -3;
                sys.playSound("ground");
                changeAttackAnim("grounded", 20);
            }
        } else if (actionAnim.equals("grounded")) {
            if (currentFrameDelay == frameDelay) {
                changeAttackAnim("get_up", 5);
            }
        }

        if (stun > 0) {
            stun -= 1;
            if ((stun == 0) && (!actionAnim.equals("juggle")) && (battle.currentEnemy == this)) {
                battle.currentEnemy = null;
                battle.comboCount = 0;
                battle.comboDamage = 0;
            }
        } else if (((actionAnim == null) || (!actionAnim.equals("juggle"))) && (battle.currentEnemy == this)) {
            battle.currentEnemy = null;
            battle.comboCount = 0;
            battle.comboDamage = 0;
        }

//        } else if (battle.currentEnemy == this) {
//            if (!actionAnim.equals("juggle")) {
//                System.out.println(actionAnim);
//            }
//        }


        element.setFlip(flipped);
//        if (stun > 0) {
//            if (frameMovement[0] > 0) {
//                flipped = false;
//            } else if (frameMovement[0] < 0) {
//                flipped = true;
//            }
//            element.setFlip(flipped);
//        }

        int[] newPos = new int[2];
        newPos[0] = (int) (getX() + frameMovement[0]);

        // fall out of border
        if (getY() > 2000) {
            newPos[0] = 500;
            newPos[1] = -500;
        } else {
            newPos[1] = (int) (getY() + frameMovement[1]);
        }

        setX(newPos[0]);
        Rectangle hitbox = getHitbox();

        int[] temp = new int[2];
        temp[0] = (int) hitbox.getCenterX();
        temp[1] = (int) hitbox.getCenterY();

        for (Tile t : Tile.tilesAround(temp, tileMap)) {
            Rectangle tileHitbox = t.getHitbox();

            if (hitbox.intersects(tileHitbox)) {
                // right
                if (frameMovement[0] > 0) {
                    setX((int) (tileHitbox.getX() - (hitbox.getWidth() / 2)));
                    collisions[3] = true;
                }
                // left
                if (frameMovement[0] < 0) {
                    setX((int) (tileHitbox.getX() + tileHitbox.getWidth() + (hitbox.getWidth() / 2)));
                    collisions[2] = true;
                }
            }
        }

        setY(newPos[1]);
        Rectangle hitbox2 = getHitbox();
        temp[0] = (int) ((int) hitbox2.getCenterX());
        temp[1] = (int) hitbox2.getCenterY();

        for (Tile t : Tile.tilesAround(temp, tileMap)) {
            Rectangle tileHitbox = t.getHitbox();
            if (hitbox2.intersects(tileHitbox)) {
                // down FIX THIS BRUHHHH
//                if (this != sys.getCurrentBattle().getPlayer()) {
//                }
                if (frameMovement[1] > 0) {
                    setY((int) tileHitbox.getY());
                    collisions[1] = true;
                }
                // up
                if (frameMovement[1] < 0) {
                    setY((int) ((int) tileHitbox.getY() + tileHitbox.getHeight() + hitbox2.getHeight()));
                    collisions[0] = true;
                }
            }
        }

        if (collisions[0] || collisions[1]) {
            velocity[1] = 0;
        }

        if (collisions[1]) {
            airTime = 0;
            doubleJump = false;
            if (animState != null) {
                if (animState.equals("jump")) {
                    sys.playSound("land");
                }
            }
        } else {
            airTime += 1;
            if (airTime == 5) {
                doubleJump = true;
            }
        }
    }
}
