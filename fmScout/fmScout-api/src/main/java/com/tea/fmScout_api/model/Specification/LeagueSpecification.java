package com.tea.fmScout_api.model.Specification;

import com.tea.fmScout_api.model.FootballPlayer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class LeagueSpecification implements Specification<FootballPlayer> {
    private String leagueName;

    public LeagueSpecification(String leagueName)
    {
        this.leagueName = leagueName;
    }

    @Override
    public Predicate toPredicate(Root<FootballPlayer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (leagueName == null) {
            return criteriaBuilder.conjunction();
        }

        return criteriaBuilder.equal(root.get("club").get("league").get("leagueName"),leagueName);
    }
}
