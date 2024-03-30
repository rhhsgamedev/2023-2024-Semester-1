import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
public class Hitbox {
    public static final int STARTUP = 0;
    public static final int ACTIVE = 1;
    public static final int RECOVERY = 2;
    public HashMap<String, Integer> data;
    public int frame;
    public int state;
    public int startup;
    public int active;
    public int recovery;
    public int xOffset;
    public int yOffset;
    public int length;
    public int height;
    public int airBoost;
    public Rectangle rect;
    public BattleChar character;
    public HashSet<BattleChar> ignore;
    public Game sys;
    public boolean finished;
    public int[] frameData;
    public String hitEffect;
    public int damage;
    public int launch;
    private Battle battle;
    public int customJuggleBehavior;

    public Hitbox(Game sys, int[] frameData, BattleChar character, String hitEffect) {
        this.finished = false;
        // x offset, y offset, length, height, startup, active, recovery
        this.frameData = frameData;
        this.sys = sys;
        this.character = character;
        this.frame = 1;
        this.state = STARTUP;
        this.hitEffect = hitEffect;
        this.ignore = new HashSet<>();
        this.battle = sys.getCurrentBattle();
        ignore.add(character);

        this.xOffset = frameData[0];
        this.yOffset = frameData[1];
        this.length = frameData[2];
        this.height = frameData[3];
        this.damage = frameData[10];
        this.launch = frameData[11];
        this.customJuggleBehavior = frameData[12];
        this.airBoost = frameData[13];

        if (character.isFlipped()) {
            this.xOffset -= (this.xOffset * 2) + this.length;
        }

        this.startup = frameData[4];
        this.active = frameData[5];
        this.recovery = frameData[6];
        this.rect = new Rectangle(character.getX() +  this.xOffset, character.getY() + this.yOffset, this.length, this.height);
    }

    public void update(HashSet<BattleChar> chars) {
        if (finished) {
            return;
        }

        rect.x = character.getX() +  this.xOffset;
        rect.y = character.getY() + this.yOffset;

        if ((frame <= this.startup)) {
            this.state = STARTUP;
        } else if ((frame > this.startup) && (frame <= this.startup + this.active)) {
            this.state = ACTIVE;
        } else if ((frame > this.startup + this.active) && (frame <= this.startup + this.active + this.recovery)) {
            this.state = RECOVERY;
        } else {
            this.finished = true;
        }

        if (this.state == ACTIVE) {
            // check hitbox
            for (BattleChar enemy : chars) {
                if (!ignore.contains(enemy)) {
                    if (this.rect.intersects(enemy.getHitbox())) {
                        if ((enemy.actionAnim == null) || ((!enemy.actionAnim.equals("get_up") && (!enemy.actionAnim.equals("grounded"))))) {

                            sys.playSound("hit" + (int) (1 + (Math.random() * 3)));
                            enemy.flipped = !character.flipped;
                            ignore.add(enemy);
                            enemy.createVFX(hitEffect, 6, 0.5, -110, -160, false);

                            BattlePlayer player = (BattlePlayer) sys.getCurrentBattle().getPlayer();
                            if (character == player) {
                                if (sys.getCurrentBattle().currentEnemy != enemy) {
                                    sys.getCurrentBattle().currentEnemy = enemy;
                                }
                                sys.getCurrentBattle().comboCount += 1;
                                sys.getCurrentBattle().comboDamage += damage;
                                sys.getCurrentBattle().lastHitDamage = damage;
                            }


                            // initial juggle
                            if ((this.launch == 1) && ((enemy.actionAnim == null) || (!enemy.actionAnim.equals("juggle")))) {
                                if (character.isFlipped()) {
                                    enemy.velocity[0] -= frameData[8];
                                } else {
                                    enemy.velocity[0] += frameData[8];
                                }
                                enemy.velocity[1] += frameData[9];
                                enemy.setStun(20);
                                battle.launchScreenEffect();
                                enemy.changeAttackAnim("juggle", 20, 1);
                                System.out.println("LAUNCHED");
                                // juggle hits
                            } else if ((!(enemy.actionAnim == null)) && (enemy.actionAnim.equals("juggle"))) {
                                enemy.changeAttackAnim("juggle", 20, 1);
                                battle.hitScreenEffect();
                                // to prevent infinite combos with light finisher lol
                                if (this.customJuggleBehavior == 1) {
                                    if (character.isFlipped()) {
                                        enemy.velocity[0] -= frameData[8];
                                    } else {
                                        enemy.velocity[0] += frameData[8];
                                    }
                                    enemy.velocity[1] = frameData[9];
                                } else {
                                    if (character.isFlipped()) {
                                        enemy.velocity[0] -= 6;
                                    } else {
                                        enemy.velocity[0] += 6;
                                    }
                                    enemy.velocity[1] = -6;
                                }

                                if (airBoost == 1) {
                                    character.canAirAttack = true;
                                    character.velocity[1] = -5;
                                    character.setWalkspeed(0);
                                    if (character.isFlipped()) {
                                        character.velocity[0] = -6;
                                    } else {
                                        character.velocity[0] = 6;
                                    }
                                }
                                // regular hits
                            } else {
                                enemy.setStun(frameData[7]);
                                battle.hitScreenEffect();
                                if (character.isFlipped()) {
                                    enemy.velocity[0] -= frameData[8];
                                } else {
                                    enemy.velocity[0] += frameData[8];
                                }
                                enemy.velocity[1] += frameData[9];
                            }
                        }
                    }
                }
            }
        }

        this.frame += 1;
    }

    public boolean isFinished() {
        return finished;
    }

    public Rectangle getRect() {
        return this.rect;
    }
}
