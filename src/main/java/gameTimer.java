/**
 * Game timer class to handle all timers for maze game
 * handles display timer, bonus respawn/deletion timers
 * also handles bonus effect and trap timers
 */
public class gameTimer {
    private double freezeTime, speedTime, trapTime, respawnTimer, deleteBonusTimer;//timers
    private double gameTime;//time playing game
    private double startTime;
    private double pauseDelay;//offset for duration paused
    private int sec, min, hour,endHour,endMin,endSec;//play time, and final game times
    private boolean finalTimeSet = false;//used to save end game times

    /**
     * initialize current time, and set the game play time
     */
    public gameTimer(){
        setStartTime();
        updateGameTime();
    }

    //update the game time according to current system time
    public double updateGameTime(){
        gameTime = (System.currentTimeMillis() - (startTime+pauseDelay))/ 1000;
        return gameTime;
    }

    //used to offset the system time taken while pausing, in order to correct for gameTime
    public void setPauseDelay() {
        pauseDelay = (System.currentTimeMillis()-startTime-gameTime*1000);//find time passed while paused (ms)
    }

    /**
     * Calculates the time remaining for an effects duration
     * used to display effects duration in game screen
     * @param type the effect, either for freeze/speed bonus or trap
     * @return the time remaining in seconds till the given effect is done
     */
    public int effectTimeLeft(String type){
        int timeLeft = 0;
        switch(type) {
            case "speed bonus":
                timeLeft = (int)(speedTime - gameTime);
                break;
            case "freeze bonus":
                timeLeft = (int)(freezeTime - gameTime);
                break;
            case "trap":
                timeLeft = (int)(trapTime - gameTime);
                break;
        }
        return timeLeft;
    }
    //system time at begining of game, used as an offset to calculate game play time
    public void setStartTime(){
        startTime = System.currentTimeMillis();
    }

    //functions that handle bonus deletion and respawn times
    public void setBonusDeleteTime(int bonusWait){
        //time that bonus should be deleted if unclaimed
        deleteBonusTimer = bonusWait+gameTime;  //start the waiting timer
    }
    public void setRespawnTime(){
        //time till next bonus will appear
        respawnTimer = gameTime+5;
    }

    //sets bonus/trap time for when their effects will be done
    //duration of effect is given as argument
    public void setFreezeTime(int bonusWait){
        freezeTime = bonusWait + gameTime;
    }
    public void setSpeedTime(int bonusWait){
        speedTime = bonusWait + gameTime;
    }

    public void setTrapTime(int trapWait){
        trapTime = trapWait + gameTime;
    }

    //bool functions that return true if a specified timer is up
    public boolean respawnTime(){
        return ((int) respawnTimer == (int)gameTime);
    }
    public boolean deleteTime(){
        //is it time to delete bonus
        return ((int) deleteBonusTimer == (int)gameTime);
    }
    public boolean frozenTimeDone(){
        return ((int) freezeTime == (int)gameTime);
    }
    public boolean speedTimeDone(){
        return ((int)speedTime == (int)gameTime);
    }
    public boolean trapTimeDone(){
        return ((int)trapTime == (int)gameTime);
    }

    //functions below simply calculate the play time for the time display
    public int getHour() {
        hour = (int)gameTime / 3600;
        return hour;
    }
    public int getSec(){
        updateGameTime();
        sec = (int)gameTime % 60;
        return sec;
    }
    public int getMin(){
        min = ((int)gameTime / 60) % 60;
        return min;
    }

    /**
     * called when the game is done, sets the final game time
     * for the end credit screen
     */
    public void stop(){
        if(!finalTimeSet){
            finalTimeSet = true;
            endHour = getHour();
            endMin = getMin();
            endSec = getSec();
        }
    }
    //below are timer values used for finish screen
    public int getEndHour(){return endHour;}
    public int getEndMin(){return endMin;}
    public int getEndSec(){return endSec;}
}
