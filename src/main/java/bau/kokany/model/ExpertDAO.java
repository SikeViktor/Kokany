package bau.kokany.model;

import java.util.List;

public interface ExpertDAO extends AutoCloseable{
    public void saveExpert(Expert e);
    public void deleteExpert(Expert e);
    public void updateExpert(Expert e);
    public List<Expert> getExperts();
}
