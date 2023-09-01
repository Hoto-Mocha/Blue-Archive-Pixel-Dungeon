package com.shatteredpixel.shatteredpixeldungeon.items.active;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Grenade extends Item {

    protected static final String AC_LIGHTTHROW = "LIGHTTHROW";

    protected int amount;
    protected int max_amount;
    public static final String TXT_STATUS = "%d/%d";

    {
        defaultAction = AC_LIGHTTHROW;
        levelKnown = true;

        unique = true;
        bones = false;
    }

    private static final String AMOUNT = "amount";
    private static final String MAX_AMOUNT = "max_amount";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(AMOUNT, amount);
        bundle.put(MAX_AMOUNT, max_amount);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        amount = bundle.getInt(AMOUNT);
        max_amount = bundle.getInt(MAX_AMOUNT);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions( hero );
        actions.add ( AC_LIGHTTHROW );
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_LIGHTTHROW)) {
            if (amount > 0) {
                usesTargeting = true;
                GameScene.selectCell( thrower );
            } else {
                usesTargeting = false;
                GLog.w(Messages.get(this, "no_left"));
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

    public int maxAmount() { //최대 장탄수
        int max = max_amount;

        max += this.buffedLvl();

        return max;
    }

    public void reload() {
        int oldAmt = amount;
        amount++;
        if (amount > maxAmount()) {
            amount = maxAmount();
        }
        Item.updateQuickslot();
        if (oldAmt != amount) {
            Dungeon.hero.yellP(Messages.get(Hero.class, Dungeon.hero.heroClass.name() + "_grenade_collect_" + (Random.Int(3)+1)));
        }
    }

    @Override
    public String status() { //아이템 칸 오른쪽 위에 나타내는 글자
        return Messages.format(TXT_STATUS, amount, maxAmount()); //TXT_STATUS 형식(%d/%d)으로, round, maxRound() 변수를 순서대로 %d부분에 출력
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public Boomer knockItem(){
        return new Boomer();
    }

    public class Boomer extends Item {

        @Override
        protected void onThrow(int cell) {
            activate(cell);
        }

        //needs to be overridden
        protected void activate(int cell) {
        }

        @Override
        public void cast(final Hero user, final int dst) {
            super.cast(user, dst);
        }
    }

    private CellSelector.Listener thrower = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                knockItem().cast(curUser, target);
                Grenade.this.amount--;
            }
        }
        @Override
        public String prompt() {
            return Messages.get(Item.class, "prompt");
        }
    };
}


