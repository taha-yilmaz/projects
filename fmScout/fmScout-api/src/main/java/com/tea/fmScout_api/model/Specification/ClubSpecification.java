package com.tea.fmScout_api.model.Specification;

import com.tea.fmScout_api.model.FootballPlayer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ClubSpecification implements Specification<FootballPlayer> {
    private String clubName; //

    public ClubSpecification(String clubName)
    {
        this.clubName = clubName;
    }

    @Override
    public Predicate toPredicate(Root<FootballPlayer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (clubName == null) {
            return criteriaBuilder.conjunction();
        }

        return criteriaBuilder.equal(root.get("club").get("clubName"), clubName);
    }
}
