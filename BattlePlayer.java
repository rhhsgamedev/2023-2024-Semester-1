import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public class BattlePlayer extends BattleChar {

    BattlePlayer(Game sys, int x, int y, String name) {
        super(sys, x, y, name);
    }

    public void update() {

        if (sys.isKeyPressed("u")) {
            int originalX = (int) ((getHitbox().getCenterX() + sys.getWorldX()) * sys.getCurrentBattle().getScaling());
            int originalY = (int) ((getHitbox().getCenterY() + sys.getWorldY()) * sys.getCurrentBattle().getScaling());
            sys.getCurrentBattle().setScaling(sys.getCurrentBattle().getScaling() * 2);
            int newX = (int) ((getHitbox().getCenterX() + sys.getWorldX()) * sys.getCurrentBattle().getScaling());
            int newY = (int) ((getHitbox().getCenterY() + sys.getWorldY()) * sys.getCurrentBattle().getScaling());

            sys.getCurrentBattle().setXZoomOffset(sys.getCurrentBattle().getXZoomOffset() + (originalX + newX));
            sys.getCurrentBattle().setYZoomOffset(sys.getCurrentBattle().getYZoomOffset() + (originalY + newY));
        }
        if (sys.isKeyPressed("o")) {
            int originalX = (int) ((getHitbox().getCenterX() + sys.getWorldX()) * sys.getCurrentBattle().getScaling());
            int originalY = (int) ((getHitbox().getCenterY() + sys.getWorldY()) * sys.getCurrentBattle().getScaling());
            sys.getCurrentBattle().setScaling(sys.getCurrentBattle().getScaling() / 2);
            int newX = (int) ((getHitbox().getCenterX() + sys.getWorldX()) * sys.getCurrentBattle().getScaling());
            int newY = (int) ((getHitbox().getCenterY() + sys.getWorldY()) * sys.getCurrentBattle().getScaling());

            sys.getCurrentBattle().setXZoomOffset(sys.getCurrentBattle().getXZoomOffset() + (originalX - newX));
            sys.getCurrentBattle().setYZoomOffset(sys.getCurrentBattle().getYZoomOffset() + (originalY - newY));
        }

        if (currentFrameDelay == frameDelay) {
            // last frame
            String animName;
            if (actionAnim == null) {
                if (charSprites.get(name + animState + String.valueOf(this.frameIndex + 1)) == null) {
                    if (loopBackTo > 0) {
                        this.frameIndex = loopBackTo;
                    } else {
                        this.frameIndex = 0;
                    }
                } else {
                    this.frameIndex += 1;
                }
                currentFrameDelay = 0;
                BufferedImage img = charSprites.get(name + animState + String.valueOf(this.frameIndex));
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

        if (stun > 0 ) {
            stun -= 1;
        }

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
            afterimages.remove(after);
            sys.getAllScreenElements().remove(after);
        }
    }

    public void attack() {
        if ((actionAnim == null) || (animCancel == true)) {
            // light
            if (sys.isKeyHeld("s")) {
                if (sys.isKeyPressed("h") && (airTime < 5)) {
                    currentLightTimer = lightTimer;
                    horiInput[0] = 0;
                    horiInput[1] = 0;
                    groundCombo += 1;

                    if (flipped) {
                        velocity[0] = -3;
                    } else {
                        velocity[0] = 3;
                    }

                    if (groundCombo == 1) {
                        createVFX("ground1", 2, 0.12, -80, -37, true);
                    }
                    if (groundCombo == 2) {
                        sys.playSound("woosh");
                        createVFX("dash", 6, 2.5, -170, -120, false);
                        createVFX("ground2", 3, 0.26, -100, -70, true);
                    } else {
                        sys.playSound("woosh2");
                    }

                    changeAttackAnim("ground" + groundCombo, 4);

                    if (groundCombo == 3) {
                        createVFX("ground3", 3, 0.27, -120, -150, true);
                        groundCombo = 0;
                        battle.getHitboxes().add(new Hitbox(sys, battle.getMoveData(name, "CrouchFinisher"), this, "Slash"));
                    } else {
                        battle.getHitboxes().add(new Hitbox(sys, battle.getMoveData(name, "LightCrouching"), this, "Physical"));
                    }
                }
            } else {
                if (sys.isKeyPressed("h")) {
                    currentLightTimer = lightTimer;

                    if ((airTime > 7) && (canAirAttack)) {
                        airCombo += 1;
                        canAirAttack = false;
                        changeAttackAnim("air" + airCombo, 5);
                        sys.playSound("shing2");

                        if (airCombo == 1) {
                            battle.getHitboxes().add(new Hitbox(sys, battle.getMoveData(name, "LightAir"), this, "Slash"));
                            createVFX("air1", 5, 0.15, -40, -120, true);
//                            if (flipped) {
//                                velocity[0] = -6;
//                            } else {
//                                velocity[0] = 6;
//                            }
//                            velocity[1] = 2;
                        } else if (airCombo == 2) {
                            battle.getHitboxes().add(new Hitbox(sys, battle.getMoveData(name, "LightAir2"), this, "Slash"));
                            createVFX("air2", 2, 0.27, -95, -130, true);
                        }
                        if (airCombo == 3) {
                            battle.getHitboxes().add(new Hitbox(sys, battle.getMoveData(name, "AirFinisher"), this, "Slash"));
                            createVFX("air3", 2, 0.27, -70, -190, true);
                            sys.playSound("hah");
                            airCombo = 0;
                        }
                    } else if (airTime < 5) {
                        horiInput[0] = 0;
                        horiInput[1] = 0;
                        lightCombo += 1;
                        if (flipped) {
                            velocity[0] = -5;
                        } else {
                            velocity[0] = 5;
                        }

                        if (lightCombo == 1) {
                            sys.playSound("huh");
                            createVFX("light1", 2, 0.18, -35, -130, true);
                        } else if (lightCombo == 2) {
                            createVFX("light2", 4, 0.26, -110, -170, true);
                        } else if (lightCombo == 3) {
                            createVFX("light3", 3, 0.27, -90, -125, true);
                        }

                        if (lightCombo == 2 || lightCombo == 3) {
                            changeAttackAnim("light" + lightCombo, 3);
                            sys.playSound("shing2");
                        } else {
                            changeAttackAnim("light" + lightCombo, 5);
                            if (lightCombo == 4) {
                                sys.playSound("shing");
                            } else {
                                sys.playSound("woosh");
                            }
                        }
                        if (lightCombo == 4) {
                            lightCombo = 0;
                            createVFX("dash", 6, 2.5, -170, -120, false);
                            createVFX("light4", 3, 0.24, -80, -145, true);
                            sys.playSound("sei");
                            battle.getHitboxes().add(new Hitbox(sys, battle.getMoveData(name, "LightFinisher"), this, "Slash"));
                        } else {
                            battle.getHitboxes().add(new Hitbox(sys, battle.getMoveData(name, "LightStanding"), this, "Slash"));
                        }
                    }
                } else if (sys.isKeyPressed("g")) {
                    horiInput[0] = 0;
                    horiInput[1] = 0;
                    changeAttackAnim("taunt", 6, 21);
                    sys.playSound("taunt");
                    // launcher
                } else if (sys.isKeyPressed("j")) {
                    horiInput[0] = 0;
                    horiInput[1] = 0;

                    if (flipped) {
                        velocity[0] = -7;
                    } else {
                        velocity[0] = 7;
                    }

                    battle.getHitboxes().add(new Hitbox(sys, battle.getMoveData(name, "Launcher"), this, "Physical"));
                    createAfterimage();
                    sys.playSound("woosh");
                    changeAttackAnim("launcher", 5);
                    createVFX("dash", 6, 2.5, -170, -120, false);
                    sys.playSound("dama");

                } else if (sys.isKeyPressed("q")) {
                    setWalkspeed(5);
                    if (flipped) {
                        velocity[0] = -8;
                    } else {
                        velocity[0] = 8;
                    }

                    createVFX("dash_ring", 7, 0.2, -100, -135, false, 0.8f);

                    velocity[1] = -2;
                    changeAttackAnim("dash", 4);
                    createAfterimages = true;
                    animCancel = true;
                    sys.playSound("dash");
                }
            }
        } else if (actionAnim != null) {
            if (sys.isKeyPressed("g")) {
                if ((actionAnim.equals("taunt")) && (loopBackTo != -1)) {
                    loopBackTo = -1;
                }
            }
        }
        if (currentLightTimer > 0) {
            currentLightTimer -= 1;
        } else {
            lightCombo = 0;
            groundCombo = 0;
            airCombo = 0;
        }
    }

    public void move(Tile[][] tileMap) {
        // cant move during attack
        if (actionAnim == null) {

            if (sys.isKeyHeld("a")) {
                horiInput[0] = -1;
                flipped = true;
            } else {
                horiInput[0] = 0;
            }
            if (sys.isKeyHeld("d")) {
                horiInput[1] = 1;
                flipped = false;
            } else {
                horiInput[1] = 0;
            }
            element.setFlip(flipped);
            if (sys.isKeyPressed("w") && (((airTime < 5) || (doubleJump)))) {
                doubleJump = !doubleJump;
                if (!doubleJump) {
                    createVFX("double_jump", 3, 0.5, -70, -100, false);
                    frameIndex = -1;
                    currentFrameDelay = 0;
                    loopBackTo = 2;
                }
                setWalkspeed(5);
                velocity[1] = -8;
            }
        }

        // up, down, left, right
        boolean[] collisions = new boolean[4];

        double[] frameMovement = new double[2];
        frameMovement[0] = ((horiInput[0] + horiInput[1]) * this.walkspeed + velocity[0]);
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
            if ((airTime > 5)) {
                changeMoveAnim("jump", 15, 2);
            } else if ((frameMovement[0] != 0)) {
                changeMoveAnim("walk", 5);
            } else if ((frameMovement[0] == 0)) {
                changeMoveAnim("idle", 10);
            }
        }

//        if (frameMovement[0] > 0) {
//            flipped = false;
//        } else if (frameMovement[0] < 0) {
//            flipped = true;
//        }
//        element.setFlip(flipped);

        int[] newPos = new int[2];
        newPos[0] = (int) (getX() + frameMovement[0]);

        // fall out of border
        if (getY() > 2000) {
            newPos[1] = -500;
            newPos[0] = 500;
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
            setWalkspeed(5);
            canAirAttack = true;
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
