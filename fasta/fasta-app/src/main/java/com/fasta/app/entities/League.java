package com.fasta.app.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.fasta.app.comparators.PlayerComparator;
import com.fasta.app.enums.Order;
import com.fasta.app.enums.Role;
import com.fasta.app.push.Bid;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(of = "name")
public class League {

	private final String name;
	private final BigDecimal globalBudget;
	private final String password;
	private final Random random = new Random();
	private Map<Role, Order> callOrderMap = new EnumMap<>(Role.class);

	private Map<Role, Set<Player>> playersMap = new EnumMap<>(Role.class);
	private Set<Team> teams;
	private Timer timer;
	private Player playerToBuy;
	private Map<Player, List<Bid>> history = new HashMap<>();

	public League(final String name, final BigDecimal budget, final String password) {
		this.name = name;
		this.globalBudget = budget;
		this.password = password;
		teams = new HashSet<>();
		PlayerComparator playerComparator = new PlayerComparator();
		playersMap.put(Role.GOALKEEPER, new TreeSet<>(playerComparator));
		playersMap.put(Role.DEFENDER, new TreeSet<>(playerComparator));
		playersMap.put(Role.MIDFIELDER, new TreeSet<>(playerComparator));
		playersMap.put(Role.FORWARD, new TreeSet<>(playerComparator));
		callOrderMap.put(Role.GOALKEEPER, Order.ALPHA);
		callOrderMap.put(Role.DEFENDER, Order.ALPHA);
		callOrderMap.put(Role.MIDFIELDER, Order.ALPHA);
		callOrderMap.put(Role.FORWARD, Order.ALPHA);
		timer = new Timer(3);
		populatePlayers();
	}

	public League(final String name, final BigDecimal budget, final String password, final Order gkOrder,
			final Order dfOrder, final Order mfOrder, final Order fwOrder) {
		this(name, budget, password);
		callOrderMap.put(Role.GOALKEEPER, gkOrder);
		callOrderMap.put(Role.DEFENDER, dfOrder);
		callOrderMap.put(Role.MIDFIELDER, mfOrder);
		callOrderMap.put(Role.FORWARD, fwOrder);
	}

	public void callAuction(Role role) {
		if (playerToBuy != null) {
			throw new IllegalArgumentException("Un'asta é in corso");
		}

		Optional<Player> player = getNextPlayer(role);
		if (player.isPresent()) {
			playerToBuy = player.get();
			history.put(playerToBuy, new ArrayList<>());
		} else {
			throw new IllegalArgumentException("Giocatori non  disponibili");
		}
	}

	public void callBid(Bid bid) {
		if (playerToBuy == null) {
			return;
		}
		if (!history.containsKey(playerToBuy)) {
			throw new IllegalArgumentException("Offerta non valida");
		}
		addBid(bid);
	}

	private void addBid(Bid bid) {
		Optional<Team> team = getTeam(bid.getTeamName());
		if (!team.isPresent()) {
			throw new IllegalArgumentException("La squadra non esiste");
		}
		if (!team.get().hasBudget(bid.getValue())) {
			throw new IllegalArgumentException("Saldo non disponibile");
		}

		Bid lastBid = findLastBid();
		if (lastBid == null || lastBid.getValue().compareTo(bid.getValue()) < 0) {
			history.get(playerToBuy).add(bid);
		} else {
			throw new IllegalArgumentException("Offerta troppo bassa");
		}
	}

	private Bid findLastBid() {
		List<Bid> list = history.get(playerToBuy);
		if (list != null && !list.isEmpty()) {
			return list.get(list.size() - 1);
		}
		return null;
	}

	public void sellPlayer() {
		if (playerToBuy == null) {
			return;
		}
		Optional<Player> player = getPlayer(playerToBuy);
		if (!player.isPresent()) {
			throw new IllegalArgumentException("Giocatore non disponibile");
		}

		Bid lastBid = findLastBid();
		BigDecimal price = BigDecimal.ZERO;
		if (lastBid != null) {
			Optional<Team> team = getTeam(lastBid.getTeamName());
			if (team.isPresent()) {
				price = lastBid.getValue();
				team.get().buyPlayer(playerToBuy, price);
			}
		}
		player.get().sell(price);
		playerToBuy = null;
	}

	public Team addTeam(String name, String password) {
		if (teams.stream().noneMatch(t -> t.getName().equalsIgnoreCase(name))) {
			Team team = new Team(name, globalBudget, password);
			teams.add(team);
			return team;
		}
		throw new IllegalArgumentException("Squadra gia presente");
	}

	public void removeTeam(String name) {
		Optional<Team> team = getTeam(name);
		if (team.isPresent()) {
			teams.remove(team.get());
		}
	}

	private Optional<Team> getTeam(String name) {
		return teams.stream().filter(t -> t.getName().equalsIgnoreCase(name)).findFirst();
	}

	public void addPlayers(Set<Player> players) {
		for (Player player : players) {
			playersMap.get(player.getRole()).add(player);
		}
	}

	public Optional<Player> getPlayer(Player player) {
		return playersMap.get(player.getRole()).stream().filter(p -> p.equals(player)).findFirst();
	}

	public Optional<Player> getNextPlayer(Role role) {
		Set<Player> set = playersMap.get(role);
		if (set == null || set.isEmpty()) {
			return Optional.empty();
		}

		Stream<Player> stream = set.stream().filter(p -> p.getPrice() == null);
		switch (callOrderMap.get(role)) {
		case RANDOM:
			long randomIndex = getRandomNumber(set.size());
			return stream.skip(randomIndex).findFirst();
		case ALPHA:
			return stream.findFirst();
		default:
			return Optional.empty();
		}
	}

	private long getRandomNumber(long size) throws AssertionError {
		return random.nextInt((int) size);
	}

	public Optional<Player> getRandomPlayer() {
		long randomList = getRandomNumber(playersMap.values().size());
		Set<Player> set = playersMap.values().stream().skip(randomList).findFirst().orElse(new HashSet<>());

		long randomIndex = getRandomNumber(set.size());
		return set.stream().skip(randomIndex).findFirst();
	}

	private void populatePlayers() {
		addPlayers(Collections.singleton(new Player("Paolo", "Rossi", Role.GOALKEEPER, "Milan")));

		addPlayers(Collections.singleton(new Player("Daniele", "Rauso", Role.DEFENDER, "Juventus")));
		addPlayers(Collections.singleton(new Player("Giorgio", "Chiellini", Role.DEFENDER, "Juventus")));
		addPlayers(Collections.singleton(new Player("Paolo", "Maldini", Role.DEFENDER, "Milan")));

		addPlayers(Collections.singleton(new Player("Marek", "Hamsik", Role.MIDFIELDER, "Napoli")));
		addPlayers(Collections.singleton(new Player("Daniele", "De Rossi", Role.MIDFIELDER, "Roma")));

		addPlayers(Collections.singleton(new Player("Ciro", "Immobile", Role.FORWARD, "Lazio")));
		addPlayers(Collections.singleton(new Player("Nicola", "Kalinic", Role.FORWARD, "Fiorentina")));
	}

}
