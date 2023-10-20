package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class StunDrone extends Buff implements ActionIndicator.Action {

    boolean canUse;

    private static final String CANUSE = "canUse";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CANUSE, canUse);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        canUse = bundle.getBoolean(CANUSE);
        if (canUse) {
            ActionIndicator.setAction(this);
        }
    }

    @Override
    public void detach() {
        super.detach();
        canUse = false;
        ActionIndicator.clearAction(this);
    }

    @Override
    public boolean act() {
        if (hero.buff(StunDroneCooldown.class) == null) {
            canUse = true;
            ActionIndicator.setAction(this);
            BuffIndicator.refreshHero();
        }
        spend(TICK);

        return true;
    }

    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.STUNDRONE;
    }

    @Override
    public int indicatorColor() {
        return 0x85A2C4;
    }

    @Override
    public void doAction() {
        GameScene.selectCell( selector );
    }

    private void droneUse(Integer target) {
        Char mainCh = Actor.findChar(target);
        ArrayList<Char> targets = new ArrayList<>();
        if (mainCh != null) {
            Buff.affect(mainCh, Blindness.class, 10f);
            Buff.affect(mainCh, Paralysis.class, 5f);
        }
        for (int i : PathFinder.NEIGHBOURS8) {
            int cell = target + i;
            Char c = Actor.findChar(cell);
            if (c != null) {
                targets.add(c);
            }
        }
        for (Char c : targets) {
            Buff.affect(c, Blindness.class, 5f);
        }
        if (hero.hasTalent(Talent.STUNDRONE_1)) {
            if (Dungeon.level.heroFOV[target]) {
                Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
            }
            PathFinder.buildDistanceMap( target, BArray.not( Dungeon.level.solid, null ), 2 );
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                    GameScene.add(Blob.seed(i, 3*hero.pointsInTalent(Talent.STUNDRONE_1), Electricity.class));
                }
            }
        }
        if (hero.hasTalent(Talent.STUNDRONE_3)) {
            Buff.affect(hero, FlashGrenade.class);
            Item.updateQuickslot();
        }

        GameScene.flash(0x80FFFFFF);
        Sample.INSTANCE.play( Assets.Sounds.BLAST );
        Buff.affect(hero, StunDroneCooldown.class).set();
        canUse = false;
        ActionIndicator.clearAction(this);
        BuffIndicator.refreshHero();
        hero.sprite.zap(target);
        hero.spendAndNext(Actor.TICK);
        Splash.at( target, 0xCCFFFFFF, 5 );
    }

    private CellSelector.Listener selector = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (Dungeon.level.heroFOV[target]) {
                    droneUse(target);
                    hero.yellI(Messages.get(Hero.class, hero.heroClass.name() + "_stundrone_use_" + (1+Random.Int(3))));
                } else {
                    hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_no_target"));
                }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };
}

