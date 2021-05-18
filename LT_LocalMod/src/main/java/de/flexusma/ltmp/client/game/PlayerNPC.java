package de.flexusma.ltmp.client.game;

import com.lilithsthrone.game.character.CharacterImportSetting;
import com.lilithsthrone.game.character.FluidStored;
import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.character.PlayerCharacter;
import com.lilithsthrone.game.character.attributes.AbstractAttribute;
import com.lilithsthrone.game.character.attributes.Attribute;
import com.lilithsthrone.game.character.body.abstractTypes.AbstractFluidType;
import com.lilithsthrone.game.character.effects.Addiction;
import com.lilithsthrone.game.character.effects.PerkCategory;
import com.lilithsthrone.game.character.fetishes.Fetish;
import com.lilithsthrone.game.character.gender.Gender;
import com.lilithsthrone.game.character.npc.NPC;
import com.lilithsthrone.game.character.npc.NPCGenerationFlag;
import com.lilithsthrone.game.character.persona.NameTriplet;
import com.lilithsthrone.game.character.persona.PersonalityTrait;
import com.lilithsthrone.game.character.race.AbstractSubspecies;
import com.lilithsthrone.game.character.race.RaceStage;
import com.lilithsthrone.game.combat.DamageType;
import com.lilithsthrone.game.dialogue.DialogueNode;
import com.lilithsthrone.game.inventory.CharacterInventory;
import com.lilithsthrone.game.occupantManagement.slave.SlaveJob;
import com.lilithsthrone.game.occupantManagement.slave.SlaveJobSetting;
import com.lilithsthrone.game.sex.SexAreaOrifice;
import com.lilithsthrone.game.sex.SexType;
import com.lilithsthrone.main.Main;
import com.lilithsthrone.rendering.SVGImages;
import com.lilithsthrone.world.AbstractWorldType;
import com.lilithsthrone.world.places.AbstractPlaceType;
import javafx.application.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.Month;
import java.util.*;

public class PlayerNPC extends NPC {
    public int uid;
    public PlayerCharacter player;

    protected PlayerNPC(boolean isImported, NameTriplet nameTriplet, String surname, String description, int age, Month birthMonth, int birthDay,
                        int level, Gender startingGender, AbstractSubspecies startingSubspecies, RaceStage stage, CharacterInventory inventory,
                        AbstractWorldType worldLocation, AbstractPlaceType startingPlace, boolean addedToContacts, NPCGenerationFlag... generationFlags) {
        super(isImported, nameTriplet, surname, description, age, birthMonth, birthDay, level, startingGender, startingSubspecies, stage, inventory, worldLocation, startingPlace, addedToContacts, generationFlags);
        return;
    }

    public PlayerNPC(PlayerCharacter player, int uid){
        this(true,
                player.getNameTriplet(),
                player.getSurname(),
                player.getDescription(),
                player.getAgeValue(),
                player.getBirthMonth(),
                player.getDayOfBirth(),
                player.getLevel(),
                player.getGender(),
                player.getSubspecies(),
                player.getRaceStage(),
                player.getInventory(),
                player.getWorldLocation(),
                player.getLocationPlace().getPlaceType(),
                true);
        this.uid = uid;
        updateData(player);
    }

    public void updateData(PlayerCharacter player){
        this.player = player;

     /*   this.personalityTraits = player.getPersonalityTraits();
        this.sexualOrientation = player.getSexualOrientation();
        this.setHistory(player.getHistory());
        this.fetishDesireMap = player.getFetishDesireMap();
        this.fetishes = new HashSet<>(player.getFetishes(false));

        this.setHeight(player.getHeightValue(),true);
        this.setFemininity(player.getFemininityValue());
        this.setMuscle(player.getMuscleValue());
        this.setBodySize(player.getBodySizeValue());

        this.setKnowsPlayerGender(true); */


        this.raceConcealed = player.isRaceConcealed();
        this.captive = player.isCaptive();
        this.petNameMap = player.getPetNameMap();
        this.ageAppearanceDifference = player.getAgeAppearanceDifference();
        this.occupation = player.getOccupation();
        this.desiredJobs = player.getDesiredJobs();
        this.personalityTraits = player.getPersonalityTraits();
        this.sexualOrientation = player.getSexualOrientation();
        this.setObedience(player.getObedienceValue());
        this.setPerkPoints(player.getPerkPoints());
     //   this.worldLocation = player.getWorldLocation();
        this.homeWorldLocation = player.getHomeWorldLocation();
     //   this.location = player.getLocation();
        this.homeLocation = player.getHomeLocation();
     //   this.globalLocation = player.getGlobalLocation();

        //move character using right command
        this.setLocation(player.getWorldLocation(), player.getLocation(), false);

        this.body = player.getBody();
        this.genderIdentity = player.getGenderIdentity();
        //add stored fluids
        for(SexAreaOrifice sao : SexAreaOrifice.values()){
            for( FluidStored stored : player.getFluidsStoredInOrifice(sao)){
                    this.addFluidStored(sao,stored);
            }
        }

        this.fleshSubspecies = player.getFleshSubspecies();
        this.inventory = player.getInventory();
        this.potionAttributes = player.getPotionAttributes();

        for(AbstractAttribute attr : Attribute.getAllAttributes()){
            float pat = player.getBaseAttributeValue(attr);
            this.setAttribute(attr,pat);
        }
        Map<AbstractAttribute,Float> battr = new HashMap<>();
        for(AbstractAttribute attr : Attribute.getAllAttributes()){
            float pat = player.getBonusAttributeValue(attr);
            battr.put(attr,pat);
        }

        this.bonusAttributes=battr;

        this.traits = player.getTraits();
        this.perks = player.getPerksMap();
        this.specialPerks = player.getSpecialPerks();
        this.fetishes = new HashSet<>(player.getFetishes(true));
        this.fetishDesireMap = player.getFetishDesireMap();

        List<Fetish> fwclothes= player.getFetishes(true);
        fwclothes.removeAll(player.getFetishes(false));
        this.fetishesFromClothing = fwclothes;

        Map<Fetish,Integer> femap = new HashMap<>();
        for (Fetish fetish :player.getFetishes(false)){
            femap.put(fetish,player.getFetishExperience(fetish));
        }
        this.fetishExperienceMap = femap;

        this.statusEffects = player.getAppliedStatusEffects();
        this.statusEffectDescriptions = player.getStatusEffectDescriptions();

        for(String id : player.getAffectionMap().keySet()){
            this.setAffection(id,player.getAffectionMap().get(id));
        }

        for(SexAreaOrifice sao : SexAreaOrifice.values()){
            this.setTimeProgressedToFinalIncubationStage(sao,player.getTimeProgressedToFinalIncubationStage(sao));
        }

        this.setTimeProgressedToFinalPregnancyStage(player.getTimeProgressedToFinalPregnancyStage());
        this.potentialPartnersAsFather = player.getPotentialPartnersAsFather();
        this.potentialPartnersAsMother = player.getPotentialPartnersAsMother();
        this.pregnantLitter = player.getPregnantLitter();
        this.incubatingLitters = player.getIncubatingLitters();
        this.littersBirthed = player.getLittersBirthed();
        this.littersFathered = player.getLittersFathered();
        this.implantedLitters = player.getLittersImplanted();
        this.incubatingLitters = player.getIncubatingLitters();
        this.littersGenerated = player.getLittersGenerated();

        this.motherId = player.getMotherId();
        this.fatherId = player.getFatherId();
        this.incubatorId = player.getIncubatorId();
        this.conceptionDate = player.getConceptionDate();

        this.ableToBeEnslaved = false;
        this.slavesOwned = player.getSlavesOwned();
        this.enslavementClothing = player.getEnslavementClothing();
        this.enslavementDialogue = player.getEnslavementDialogue(player.getEnslavementClothing());
        this.slavePermissionSettings = player.getSlavePermissionSettings();
        List<SlaveJob> sjobs = new ArrayList<>();
        for(int i = 0; i<24;i++){
            this.setSlaveJob(i,player.getSlaveJob(i));
            sjobs.add(player.getSlaveJob(i));
        }
        for(SlaveJob sJ : sjobs){
            for(SlaveJobSetting sjset : player.getSlaveJobSettings(sJ))
                this.addSlaveJobSettings(sJ,sjset);

        }

         /*
         companions disable currently
          */

        this.combatBehaviour = player.getCombatBehaviour();
        this.equippedMoves = player.getEquippedMoves();
        this.knownMoves = player.getAvailableMoves();
        this.selectedMoves = player.getSelectedMoves();
        Map<DamageType, Integer> shields = new HashMap<>();
        for(DamageType dtype : DamageType.values()){
            shields.put(dtype,player.getShields(dtype));
        }
        this.shields = shields;
        this.remainingAP = player.getRemainingAP();
        this.spells = player.getSpells();
        this.health = player.getHealth();
        this.mana = player.getMana();

        this.setTotalOrgasmCount(player.getTotalOrgasmCount());
        this.setDaysOrgasmCount(player.getDaysOrgasmCount());
        this.setDaysOrgasmCountRecord(player.getDaysOrgasmCountRecord());
        this.lastTimeHadSex = player.getLastTimeHadSex();
        this.lastTimeOrgasmed = player.getLastTimeOrgasmed();
        this.setFoughtPlayerCount(player.getFoughtPlayerCount());
        this.setLostCombatCount(player.getLostCombatCount());
        this.setWonCombatCount(player.getWonCombatCount());

        this.sexCount = player.getSexCountMap();
        this.setAnalVirgin(player.isAnalVirgin());
        this.setAssVirgin(player.isAssVirgin());
        this.setFaceVirgin(player.isFaceVirgin());
        this.setNippleVirgin(player.isNippleVirgin());
        this.setPenisVirgin(player.isPenisVirgin());
        this.setSpinneretVirgin(player.isSpinneretVirgin());
        this.setUrethraVirgin(player.isUrethraVirgin());
        this.setVaginaVirgin(player.isVaginaVirgin());
        this.setNippleCrotchVirgin(player.isNippleCrotchVirgin());
        this.setSecondUrethraVirgin(player.isSecondUrethraVirgin());
        this.setVaginaUrethraVirgin(player.isVaginaUrethraVirgin());
        if(!player.isSlave()) this.setOwner("");

        this.setAlcoholLevel(player.getAlcoholLevelValue());
        for(Addiction ad :player.getAddictions())
            this.addAddiction(ad);
        for(AbstractFluidType fl: player.getPsychoactiveFluidsIngested())
            this.addPsychoactiveFluidIngested(fl);

        if(player.getDice()!=null)
            this.setDice( player.getDice());



    }

    @Override
    public SexType getCurrentSexPreference(GameCharacter target) {
        return player.getCurrentSexPreference(target);
    }

    @Override
    public String getMapIcon() {
        return SVGImages.SVG_IMAGE_PROVIDER.getPlayerMapIconAndrogynous();
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public void setStartingBody(boolean setPersona) {
        return;
    }

    @Override
    public void loadFromXML(Element parentElement, Document doc, CharacterImportSetting... settings) {
        return;
    }

    @Override
    public void changeFurryLevel() {
        return;
    }

    @Override
    public DialogueNode getEncounterDialogue() {
        return null;
    }

    public void updateRender(){
        Platform.runLater(()->{
            Main.game.updateResponses();

            Main.mainController.updateUILeftPanel();
            Main.mainController.updateUIRightPanel();
            Main.mainController.updateDialogue();
            }
        );
    }


    /*
    Override save function so no multiplayer data is saved
    -> save files stay compatible with the base Game.
     */

    @Override
    public Element saveAsXML(Element parentElement, Document doc) {
        Element e = doc.createElement("mutliplayerdata_placeholder");
        //parentElement.appendChild(e);
        return e;
    }
}
