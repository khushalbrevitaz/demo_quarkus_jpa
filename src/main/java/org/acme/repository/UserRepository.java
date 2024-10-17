package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.acme.dto.GetUserCountCreationPerCompanyPerYear;
import org.acme.entity.User;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    private static final Logger LOG = Logger.getLogger(UserRepository.class);
    @Inject
    private EntityManager entityManager;

    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public List<GetUserCountCreationPerCompanyPerYear> getCompanyByUserCount(List<Integer> companyIds) {

        System.out.println(companyIds);
        // SQL query
        String sql = "SELECT companyId, EXTRACT(YEAR FROM createDate) as year, COUNT(*) as userCount "
                + "FROM User WHERE companyId IN :companyIds "
                + "GROUP BY companyId, year "
                + "ORDER BY companyId, year";


        // Create query object
        Query query = entityManager.createQuery(sql);
        query.setParameter("companyIds", companyIds);

        // Get the result list and map it to the custom model
        List<Object[]> results = query.getResultList();
        LOG.info("Received " + results.size() + " objects");


        return results.stream().map(result -> new GetUserCountCreationPerCompanyPerYear(((Number) result[0]).intValue(),  // Cast to Number first, then convert to int
                result[1].toString(),              // Convert the year to a String
                ((Number) result[2]).intValue()     // userCount
        )).collect(Collectors.toList());
    }
}
