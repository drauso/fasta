package com.fasta.app.entities;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.fasta.app.comparators.PlayerComparator;
import com.fasta.app.enums.Order;
import com.fasta.app.enums.Role;

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

	private Map<Role, Order> callOrderMap = new EnumMap<>(Role.class);
	private Map<Role, Set<Player>> playersMap = new EnumMap<>(Role.class);
	private Set<Team> teams;

	private final Random random = new Random();

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
	}

	public League(final String name, final BigDecimal budget, final String password, final Order gkOrder,
			final Order dfOrder, final Order mfOrder, final Order fwOrder) {
		this(name, budget, password);
		callOrderMap.put(Role.GOALKEEPER, gkOrder);
		callOrderMap.put(Role.DEFENDER, dfOrder);
		callOrderMap.put(Role.MIDFIELDER, mfOrder);
		callOrderMap.put(Role.FORWARD, fwOrder);
	}

	public void addTeam(Team team) {
		if (teams.contains(team)) {
			throw new IllegalArgumentException("Squadra gia presente");
		}
		teams.add(team);
	}

	public void addPlayers(Set<Player> players) {
		for (Player player : players) {
			playersMap.get(player.getRole()).add(player);
		}
	}

	public void removeTeam(Team team) {
		teams.remove(team);
	}

	public Optional<Player> getPlayer(Player player) {
		return playersMap.get(player.getRole()).stream().filter(p -> p.equals(player)).findFirst();
	}

	public Optional<Player> getNextPlayer(Role role) {
		Set<Player> set = playersMap.get(role);

		if (set.isEmpty()) {
			return null;
		}

		Stream<Player> stream = set.stream();
		switch (callOrderMap.get(role)) {
		case RANDOM:
			long randomIndex = getRandomNumber(set.size());
			return stream.skip(randomIndex).findFirst();
		case ALPHA:
			return stream.findFirst();
		default:
			return null;
		}
	}

	private long getRandomNumber(long size) throws AssertionError {
		return random.nextInt((int) size);
	}

	public Optional<Player> getRandomPlayer() {
		long randomList = getRandomNumber(playersMap.values().size());
		Set<Player> set = playersMap.values().stream().skip(randomList).findFirst().get();
		if (set.isEmpty()) {
			return null;
		}
		long randomIndex = getRandomNumber(set.size());
		return set.stream().skip(randomIndex).findFirst();
	}
}
