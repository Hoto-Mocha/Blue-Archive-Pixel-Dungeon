package com.shatteredpixel.shatteredpixeldungeon.items;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
public class AmmoBox extends Item {

    {
        unique = true;

        image = ItemSpriteSheet.AMMO_BOX;
    }

    private int amount;

    public AmmoBox() {}

    public AmmoBox(int amount) {
        this.amount = amount;
    }

    private static final String AMOUNT = "amount";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(AMOUNT, amount);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        amount = bundle.getInt(AMOUNT);
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        Gun gun = null;

        if (hero.belongings.weapon instanceof Gun) {
            gun = (Gun)hero.belongings.weapon;
        }
        if (gun != null) {
            if (!gun.isAllLoaded()) {
                gun.manualReload();
                GLog.p(Messages.get(Hero.class, "ammo_reload"));
                hero.busy();
                hero.sprite.operate(pos);
                Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
                amount--;
            } else {
                GLog.p(Messages.get(Hero.class, "cannot_reload"));
            }
        }
        if (amount <= 0) {
            Heap heap = Dungeon.level.heaps.get(pos);
            if (heap != null)
                heap.remove(this);
        }
        return false;
    }

    @Override
    public String info() {
        String info = super.info();
        info += "\n\n" + Messages.get(this, "desc_ammo", this.amount);
        return info;
    }

    public static class AmmoBoxCooldown extends Buff {

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
            icon.hardlight(0x808080);
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
            maxCoolDown = 5;
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
}
