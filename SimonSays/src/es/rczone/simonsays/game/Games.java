package es.rczone.simonsays.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Games {
	
	private static Games INSTANCE;
	private HashMap<Integer,Game> aliveGames;
	private HashMap<Integer,Game> finishedGames;
	
	
	private Games(){};
	
	private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new Games();
        }
    }
 
    public static Games getInstance() {
        createInstance();
        return INSTANCE;
    }
	
	public void newGame(int key, Game game){
		aliveGames.put(key, game);	
	}
	
	public void finishGame(int key){
		Game game = aliveGames.remove(key);
		game.finish();
		finishedGames.put(key, game);
	}
	
	public Set<Integer> getKeys(){
		return aliveGames.keySet();
	}
	
	public Collection<Game> getHistorialGames(){
		
		List<Game> list = new ArrayList<Game>();
		list.addAll(aliveGames.values());
		list.addAll(finishedGames.values());
		
		return list;
	}
	
	public Collection<Game> getAliveGames(){
		return aliveGames.values();		
	}
	
	public Collection<Game> getFinishedGames(){
		return finishedGames.values();
	}
	
	public Game getAliveGame(int key){
		
		if(aliveGames.containsKey(key)){
			return aliveGames.get(key);
		}
		else{
			return null;//TODO exception
		}
	}
	
//	public void initGame(int key){
//		Game game = aliveGames.get(key);
//		game.init();
//	}
//	
//	public void requestGame(int key){
//		Game game = aliveGames.get(key);
//		game.acceptedRequestGame();
//	}
//	
//	public void acceptedRequest(int key){
//		Game game = aliveGames.get(key);
//		game.requestGame();
//	}
//	
//	public void refusedRequest(int key){
//		Game game = aliveGames.get(key);
//		game.refusedRequestGame();
//	}
//	
//	public void playingGame(){
//		
//	}
//	public void waitingForOpponentPlayer(){
//		
//	}
//	public void finishedGame(){
//		
//	}
	

}
