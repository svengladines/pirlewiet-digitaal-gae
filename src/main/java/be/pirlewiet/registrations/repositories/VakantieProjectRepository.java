package be.pirlewiet.registrations.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import be.pirlewiet.registrations.model.VakantieProject;

@Repository
public class VakantieProjectRepository extends AbstractRepository<VakantieProject> {
    @SuppressWarnings("unchecked")
    public List<VakantieProject> findVakantieProjectWithBegindateEinddateVakantietype(VakantieProject vp) {
        String query = "SELECT DISTINCT vp FROM VakantieProject vp "
                + " WHERE vp.vakantietype = :type ";
        if (vp.getEindDatum() == null) {
            query += " and vp.eindDatum = null ";
        } else {
            query += " and vp.eindDatum = :eindd ";
        }
        if (vp.getBeginDatum() == null) {
            query += " and vp.beginDatum = null ";
        } else {
            query += " and vp.beginDatum = :begind ";
        }

        Query q = em.createQuery(query);

        if (vp.getEindDatum() != null) {
            q.setParameter("eindd", vp.getEindDatum());
        }
        if (vp.getBeginDatum() != null) {
            q.setParameter("begind", vp.getBeginDatum());
        }
        q.setParameter("type", vp.getVakantietype());

        return new ArrayList<VakantieProject>(q.getResultList());
    }

    public List<VakantieProject> findVakantieProjectWithVakantietype(VakantieProject vp) {
        Query q = em.createQuery("SELECT DISTINCT vp FROM VakantieProject vp "
                + "WHERE vp.vakantietype = :type ");
        q.setParameter("type", vp.getVakantietype());
        return new ArrayList<VakantieProject>(q.getResultList());
    }
}
