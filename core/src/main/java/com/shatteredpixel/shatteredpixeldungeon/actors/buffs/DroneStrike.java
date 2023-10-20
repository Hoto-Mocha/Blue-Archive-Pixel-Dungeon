package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.scalingDepth;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DroneStrike extends Buff implements ActionIndicator.Action {

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
        if (hero.buff(DroneStrikeCooldown.class) == null) {
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
        return HeroIcon.DRONESTRIKE;
    }

    @Override
    public int indicatorColor() {
        return 0x85A2C4;
    }

    @Override
    public void doAction() {
        if (hero.hasTalent(Talent.DRONESTRIKE_2) && hero.buff(Talent.ScanCooldown.class) == null) {
            Buff.affect(hero, MagicalSight.class, 1);
            Buff.affect(hero, Talent.ScanCooldown.class, 60-10*hero.pointsInTalent(Talent.DRONESTRIKE_2));
        }
        GameScene.selectCell( selector );
    }

    private void droneUse(Integer target) {
        boolean reUse = false;
        Char mainCh = Actor.findChar(target);

        if (mainCh != null) {
            Buff.affect(mainCh, Slow.class, 10f);
            Buff.affect(mainCh, Vulnerable.class, 5f);
            int dmg = Random.NormalIntRange(hero.lvl*2 + scalingDepth(), hero.lvl*3 + 2*scalingDepth());
            if (mainCh.HP < dmg && Random.Float() < 0.4f*hero.pointsInTalent(Talent.DRONESTRIKE_3)/3f) {
                reUse = true;
            }
            mainCh.damage(dmg, hero);
        }

        if (hero.hasTalent(Talent.DRONESTRIKE_1)) {
            ArrayList<Char> targets = new ArrayList<>();
            for (int i : PathFinder.NEIGHBOURS8) {
                int cell = target + i;
                Char c = Actor.findChar(cell);
                if (c != null) {
                    targets.add(c);
                }

                if (Dungeon.level.heroFOV[cell]) {
                    CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 4);
                }

                if (Dungeon.level.flamable[cell]) {
                    Dungeon.level.destroy(cell);
                    GameScene.updateMap(cell);
                }
            }
            for (Char c : targets) {
                int dmg = Random.NormalIntRange(3*hero.pointsInTalent(Talent.DRONESTRIKE_1), 20+10*hero.pointsInTalent(Talent.DRONESTRIKE_1));
                c.damage(dmg, hero);
            }
        }

        if (!reUse) {
            Buff.affect(hero, DroneStrikeCooldown.class).set();
            canUse = false;
            ActionIndicator.clearAction(this);
            BuffIndicator.refreshHero();
        }

        if (Dungeon.level.heroFOV[target]) {
            CellEmitter.center(target).burst(BlastParticle.FACTORY, 30);
        }
        Sample.INSTANCE.play( Assets.Sounds.BLAST );
        hero.sprite.zap(target);
        hero.spendAndNext(Actor.TICK);
    }

    private CellSelector.Listener selector = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (Dungeon.level.heroFOV[target]) {
                    droneUse(target);
                    hero.yellI(Messages.get(Hero.class, hero.heroClass.name() + "_dronestrike_use_" + (1+Random.Int(3))));
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

