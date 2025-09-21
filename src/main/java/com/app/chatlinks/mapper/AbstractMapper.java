package com.app.chatlinks.mapper;

import com.app.chatlinks.dto.GenericDTO;
import com.app.chatlinks.mysql.model.GenericModel;
import org.springframework.util.CollectionUtils;

import java.util.*;

public abstract class AbstractMapper<V extends GenericDTO, M extends GenericModel> implements IMapper<V, M> {

    protected static <V> List<V> newListInstance(final Collection<?> objects) {
        List<V> result = null;
        if (!CollectionUtils.isEmpty(objects)) {
            result = new ArrayList<V>(objects.size());
        }
        if(result == null) {
            result = new ArrayList<V>();
        }
        return result;
    }
    private static <V> Set<V> newSetInstance(final Collection<?> objects) {
        Set<V> result = null;
        if (!CollectionUtils.isEmpty(objects)) {
            result = new HashSet<V>(objects.size());
        }
        return result;
    }
    public List<M> mapToModelList(final Collection<V> transfers) {
        final List<M> result = newListInstance(transfers);
        if (result != null) {
            for (V transfer : transfers) {
                result.add(mapToModel(transfer));
            }
        }
        return result;
    }
    public List<V> mapToDTOList(final Collection<M> entities) {
        final List<V> result = newListInstance(entities);
        if (result != null) {
            for (M entity : entities) {
                result.add(mapToDTO(entity));
            }
        }
        return result;
    }
    /*
     * (non-Javadoc)
     *
     * @see com.os.sp.mapper.IMapper#mapEntitySetBrief(java.util.Set)
     */
    public Set<M> mapToModelSet(final Collection<V> transfers) {
        final Set<M> result = newSetInstance(transfers);
        if (result != null) {
            for (V transfer : transfers) {
                result.add(mapToModel(transfer));
            }
        }
        return result;
    }
    /*
     * (non-Javadoc)
     *
     * @see com.os.sp.mapper.IMapper#mapTransferSetBrief(java.util.Set)
     */
    public Set<V> mapToDTOSet(final Collection<M> entities) {
        final Set<V> result = newSetInstance(entities);
        if (result != null) {
            for (M entity : entities) {
                result.add(mapToDTO(entity));
            }
        }
        return result;
    }
    /*
     * (non-Javadoc)
     * @see com.os.sp.mapper.IMapper#mapEntityClone(java.lang.Object)
     */
    public M mapToModelClone(final V transfer) {
        return mapToModel(transfer);
    }

    /*
     * (non-Javadoc)
     * @see com.os.sp.mapper.IMapper#mapTransferClone(java.lang.Object)
     */
    public V mapToDTOClone(final M entity) {
        return mapToDTO(entity);
    }

    /**
     * Used to clone objects.
     *
     * *** BEFORE USAGE *** Check if mapper overrides default method implementation.
     *
     * @param entity entity to clone.
     * @return cloned entity.
     */
    public M cloneObject(final M entity) {
        return entity;
    }

    public Set<M> cloneObjectsSet(final Set<M> entities) {
        final Set<M> result = newSetInstance(entities);
        if (result != null) {
            for (M entity : entities) {
                result.add(cloneObject(entity));
            }
        }
        return result;
    }
    public List<M> cloneObjectsList(final List<M> entities) {
        final List<M> result = newListInstance(entities);
        if (result != null) {
            for (M entity : entities) {
                result.add(cloneObject(entity));
            }
        }
        return result;
    }
    String calculateTIme(long timeDifferenceMilliseconds) {
        long diffSeconds = timeDifferenceMilliseconds / 1000;
        long diffMinutes = timeDifferenceMilliseconds / (60 * 1000);
        long diffHours = timeDifferenceMilliseconds / (60 * 60 * 1000);
        long diffDays = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24);
        long diffWeeks = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24 * 7);
        long diffMonths = (long) (timeDifferenceMilliseconds / (60 * 60 * 1000 * 24 * 30.41666666));
        long diffYears = timeDifferenceMilliseconds / ((long)60 * 60 * 1000 * 24 * 365);

        if (diffSeconds < 1) {
            return "less than a second ago";
        } else if (diffMinutes < 1) {
            return diffSeconds + " seconds ago";
        } else if (diffHours < 1) {
            return diffMinutes + " minutes ago";
        } else if (diffDays < 1) {
            return diffHours + " hours ago";
        } else if (diffWeeks < 1) {
            return diffDays + " days ago";
        } else if (diffMonths < 1) {
            return diffWeeks + " weeks ago";
        } else if (diffYears < 1) {
            return diffMonths + " months ago";
        } else {
            return diffYears + " years ago";
        }
    }
}