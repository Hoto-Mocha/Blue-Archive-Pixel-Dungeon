package com.shatteredpixel.shatteredpixeldungeon.items.active;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Laser extends Item {
    {
        image = ItemSpriteSheet.LASER;
        levelKnown = true;

        defaultAction = AC_USE;
        usesTargeting = false;

        bones = false;
        unique = true;
    }

    private static final String AC_USE = "USE";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_USE );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute( hero, action );
        if (action.equals(AC_USE)) {
            if (Dungeon.hero.buff(LaserCooldown.class) == null) {
                usesTargeting = true;
                GameScene.selectCell( targeter );
            } else {
                Dungeon.hero.yellN(Messages.get(Hero.class, hero.heroClass.name() + "_laser_cooldown"));    //"레이저 충전 중이에요. 그렇게 보채도 소용 없답니다?"
            }
        }
    }

    @Override
    public int level() {
        int level = Dungeon.hero == null ? 0 : Dungeon.hero.lvl/5;
        return level;
    }

    @Override
    public int buffedLvl() {
        //level isn't affected by buffs/debuffs
        return level();
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public static class LaserTargeted extends Buff {
        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.INVERT_MARK;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0.8f, 0f, 0f);
        }
    }

    public static class LaserCooldown extends Buff {

        {
            type = buffType.NEUTRAL;
            announced = false;
        }

        private int coolDown;
        private int maxCoolDown;

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x548CFD);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (maxCoolDown - coolDown)/ maxCoolDown);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(coolDown);
        }

        public void kill() {
            coolDown --;
            if (coolDown <= 0) {
                detach();
            }
            BuffIndicator.refreshHero();    //영웅의 버프창 갱신
        }

        public void set() {
            maxCoolDown = 10;
            coolDown = maxCoolDown;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", coolDown, maxCoolDown);
        }

        private static final String MAXCOOLDOWN = "maxCoolDown";
        private static final String COOLDOWN  = "cooldown";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(MAXCOOLDOWN, maxCoolDown);
            bundle.put(COOLDOWN, coolDown);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            maxCoolDown = bundle.getInt( MAXCOOLDOWN );
            coolDown = bundle.getInt( COOLDOWN );
        }

    }

    private CellSelector.Listener targeter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (!Dungeon.level.heroFOV[target]) {
                    hero.yellW( Messages.get(Hero.class, hero.heroClass.name() + "_out_of_fov"));   //"보이지 않는 곳이에요."
                    return;
                }
                Char ch = Actor.findChar(target);
                if (ch != null) {
                    if (ch.alignment != Char.Alignment.ENEMY) {
                        hero.yellW( Messages.get(Hero.class, hero.heroClass.name() + "_no_enemy"));   //"적대적인 대상에게만 조준이 가능해요."
                        return;
                    }
                    Buff.affect(ch, LaserTargeted.class);           //대상에게 레이저 조준 버프 부여
                    Buff.affect(hero, LaserCooldown.class).set();   //영웅에게 쿨타임 버프 부여
                    hero.busy();                                    //액션 중에 다른 행동을 불가능하게 만듦
                    hero.sprite.zap(target);                        //영웅이 무기를 휘두르는 모션
                    hero.spendAndNext(Actor.TICK);                  //1턴 소모
                    Sample.INSTANCE.play(Assets.Sounds.RAY);        //빔 종류가 나올 때 나는 소리 발생
                }
            }
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };
}
