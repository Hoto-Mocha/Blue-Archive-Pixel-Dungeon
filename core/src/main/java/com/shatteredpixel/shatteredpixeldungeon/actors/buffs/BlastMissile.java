package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;

public class BlastMissile extends Buff implements ActionIndicator.Action {

    public int floor;
    public int initialFloor;

    private static final String FLOOR = "floor";
    private static final String INITIAL_FLOOR = "initialFloor";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FLOOR, floor);
        bundle.put(INITIAL_FLOOR, initialFloor);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        floor = bundle.getInt(FLOOR);
        initialFloor = bundle.getInt(INITIAL_FLOOR);
        ActionIndicator.setAction(this);
    }

    public void hit(Mob mob) {
        Buff.affect(mob, BlastMissileAttach.class);
        ActionIndicator.setAction(this);
        initialFloor = Dungeon.depth;
        floor = initialFloor;
    }

    @Override
    public boolean act() {
        floor = Dungeon.depth;
        boolean exists = false;
        for (Char c : Actor.chars()) {
            if (c instanceof Mob && c.buff(BlastMissileAttach.class) != null) {
                exists = true;
                break;
            }
        }
        if (floor != initialFloor || !exists) {
            detach();
        }
        spend(TICK);

        return true;
    }

    public void mobsUpdate(Mob mob) {
        boolean exists = false;
        for (Char c : Actor.chars()) {
            if (c instanceof Mob && c != mob && c.buff(BlastMissileAttach.class) != null) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            detach();
            ActionIndicator.clearAction();
        }
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.BLAST_MISSILE;
    }

    @Override
    public int indicatorColor() {
        return 0xFDA082;
    }

    @Override
    public void doAction() {
        ArrayList<Mob> mobs = new ArrayList<>();
        for (Char c : Actor.chars()) {
            if (c instanceof Mob && c.buff(BlastMissileAttach.class) != null) {
                mobs.add((Mob)c);
            }
        }
        if (!mobs.isEmpty()) {
            for (Mob mob : mobs) {
                BlastMissileAttach attached = mob.buff(BlastMissileAttach.class);
                int dmg = Random.NormalIntRange(5, 15);
                if (hero.hasTalent(Talent.EXPLOSION_2)) {
                    Shocking shocking = new Shocking();
                    mob.damage(dmg, shocking);

                    ArrayList<Char> affected = new ArrayList<>();
                    ArrayList<Lightning.Arc> arcs = new ArrayList<>();

                    affected.clear();
                    arcs.clear();

                    Shocking.arc(hero, mob, 2, affected, arcs);

                    affected.remove(mob); //defender isn't hurt by lightning
                    for (Char ch : affected) {
                        if (ch.alignment != hero.alignment) {
                            ch.damage(Random.IntRange(1, 3), shocking);
                        }
                    }

                    hero.sprite.parent.addToFront( new Lightning( arcs, null ) );
                    Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
                } else {
                    dmg -= mob.drRoll();
                    mob.damage(dmg, hero);
                }
                if (!hero.hasTalent(Talent.EXPLOSION_2) && Dungeon.level.heroFOV[mob.pos]) {
                    CellEmitter.center(mob.pos).burst(BlastParticle.FACTORY, 30);
                    CellEmitter.get(mob.pos).burst(SmokeParticle.FACTORY, 4);
                    Sample.INSTANCE.play(Assets.Sounds.BLAST, 1, Random.Float(0.7f, 1.2f));
                }
                if (hero.hasTalent(Talent.EXPLOSION_3) && Random.Float() < 3/(float)hero.pointsInTalent(Talent.EXPLOSION_3)) {
                    Buff.affect(mob, Burning.class).reignite(mob);
                }
                attached.detach();
            }

            hero.yellI(Messages.get(Hero.class, hero.heroClass.name() + "_blast_use_" + (1+Random.Int(3)))); //"비기! 폭발 수리검!", "갑니다! (샥 샥 샥 착) 인법! 폭발의 술!", "인술! 보여드리죠!"
            hero.busy();
            hero.sprite.operate(hero.pos);
            hero.spendAndNext(2f);
        }

        detach();
    }

    public static class BlastMissileAttach extends Buff {

        @Override
        public boolean act() {
            if (hero.buff(BlastMissile.class) == null || hero.buff(BlastMissile.class).floor != hero.buff(BlastMissile.class).initialFloor) {
                detach();
            }
            spend(TICK);
            return true;
        }
    }
}
