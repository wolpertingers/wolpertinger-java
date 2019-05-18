package ingokuba.wolpertinger.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MultivaluedMap;

import lombok.Getter;
import lombok.Setter;

public abstract class Repository<E>
{

    private static final String ORDER_BY    = "orderBy";
    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_SIZE   = "pageSize";

    @Getter
    @Setter
    @PersistenceContext
    private EntityManager       entityManager;
    private Class<E>            entityType;

    public Repository(Class<E> entityType)
    {
        this.entityType = entityType;
    }

    public List<E> getEntities()
    {
        return entityManager.createQuery("select e from " + entityType.getSimpleName() + " e", entityType).getResultList();
    }

    public E get(Object primaryKey)
    {
        return entityManager.find(entityType, primaryKey);
    }

    public E store(E entity)
    {
        try {
            entityManager.persist(entity);
            if (entityManager.isJoinedToTransaction()) {
                entityManager.flush();
            }
            return entity;
        } catch (Exception e) {
            throw new RepositoryException("Failed to store " + entityType.getSimpleName(), e);
        }
    }

    public void delete(E entity)
    {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    public E update(E entity)
    {
        return entityManager.merge(entity);
    }

    public List<E> search(MultivaluedMap<String, String> queryParameters)
    {
        if (queryParameters == null || queryParameters.isEmpty()) {
            return getEntities();
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteria = builder.createQuery(entityType);
        Root<E> root = criteria.from(entityType);
        criteria.select(root);
        List<Predicate> restrictions = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        int pageNumber = 1;
        int pageSize = 10;
        for (Entry<String, List<String>> entry : queryParameters.entrySet()) {
            String key = entry.getKey();
            if (ORDER_BY.equalsIgnoreCase(entry.getKey())) {
                setOrderBy(entry, orders, builder, root);
            }
            setValue(queryParameters, builder, root, restrictions, key);
            pageSize = PAGE_SIZE.equalsIgnoreCase(key) ? intValue(queryParameters.getFirst(key), pageSize) : pageSize;
            pageNumber = PAGE_NUMBER.equalsIgnoreCase(key) ? intValue(queryParameters.getFirst(key), pageNumber) : pageNumber;
        }
        if (!restrictions.isEmpty()) {
            criteria.where(restrictions.size() > 1 ? builder.and(restrictions.toArray(new Predicate[0])) : restrictions.get(0));
        }
        criteria.orderBy(orders.isEmpty() ? defaultOrder(builder, root) : orders);
        TypedQuery<E> query = entityManager.createQuery(criteria);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    /**
     * Get the integer value of a string.
     * 
     * @return defaultValue when string cannot be formatted.
     */
    private int intValue(String string, int defaultValue)
    {
        try {
            return Integer.valueOf(string);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Order by id by default.
     * 
     * @return List with single {@link Order} entry.
     */
    private List<Order> defaultOrder(CriteriaBuilder builder, Root<E> root)
    {
        List<Order> order = new ArrayList<>();
        order.add(builder.asc(root.get("id")));
        return order;
    }

    /**
     * Create list of orderBy with the given entry.
     * Entry value examples:
     * <ul>
     * <li>{@code name} -> descending
     * <li>{@code +name} -> descending
     * <li>{@code -name} -> ascending
     * 
     * @param entry Is valid collection of orderBy when key is equal to 'orderby' (ignoring case).
     * @param orders List of {@link Order} to append to.
     */
    private void setOrderBy(Entry<String, List<String>> entry, List<Order> orders, CriteriaBuilder builder, Root<E> root)
    {
        for (String value : entry.getValue()) {
            String attribute = value.startsWith("+") || value.startsWith("-") ? value.substring(1) : value;
            if (!isAttribute(root, attribute)) {
                continue;
            }
            if (!value.startsWith("-")) {
                orders.add(builder.desc(root.get(attribute)));
            }
            if (value.startsWith("-")) {
                orders.add(builder.asc(root.get(attribute)));
            }
        }
    }

    private void setValue(MultivaluedMap<String, String> queryParameters, CriteriaBuilder builder, Root<E> root, List<Predicate> restrictions,
                          String key)
    {
        if (isAttribute(root, key)) {
            for (String value : queryParameters.get(key)) {
                restrictions.add(builder.like(root.get(key), wildcard(value)));
            }
        }
    }

    /**
     * Check whether object contains given attribute.
     * 
     * @param root Representation of the java object.
     * @param attributeName Name of the Attribute.
     */
    private boolean isAttribute(Root<E> root, String attributeName)
    {
        try {
            root.get(attributeName);
        } catch (IllegalArgumentException exception) {
            return false;
        }
        return true;
    }

    /**
     * Surround string with '%' wildcards for SQL query.
     * 
     * @param string String with missing wildcards, e.g. '%duck'
     * @return String with wildcards, e.g. '%duck%'
     */
    private static String wildcard(String string)
    {
        if (!string.startsWith("%")) {
            string = "%" + string;
        }
        if (!string.endsWith("%")) {
            string = string + "%";
        }
        return string;
    }
}
