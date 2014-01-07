package be.pirlewiet.registrations.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import be.pirlewiet.registrations.AbstractTransactionalTest;
import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.model.Vakantietype;
import be.pirlewiet.registrations.repositories.VakantieProjectRepository;
import be.pirlewiet.registrations.repositories.VakantietypeRepository;
import be.pirlewiet.registrations.utils.DateUtils;


public class VakantieProjectRepositoryTest extends AbstractTransactionalTest {
	@Autowired
	private VakantieProjectRepository vakantieProjectRepository;

	@Autowired
	private VakantietypeRepository vakantietypeRepository;

	@Autowired
	private DateUtils dateUtils;

	private VakantieProject vp1 = new VakantieProject();
	private VakantieProject vp2 = new VakantieProject();
	private VakantieProject vp3 = new VakantieProject();
	private VakantieProject vp4 = new VakantieProject();
	private VakantieProject vp5 = new VakantieProject();
	private VakantieProject vp6 = new VakantieProject();
	private VakantieProject vp7 = new VakantieProject();
	private VakantieProject vp8 = new VakantieProject();
	private Date bd1;
	private Date ed1;
	private Date bd2;
	private Date ed2;

	private Vakantietype vt1;
	private Vakantietype vt2;

	@Before
	public void init() {
		bd1 = dateUtils.toDate("01/01/2000");
		ed1 = dateUtils.toDate("01/12/2000");
		bd2 = dateUtils.toDate("03/01/2000");
		ed2 = dateUtils.toDate("03/12/2000");

		/*
		 * vakantietypeRepository.create(vt1);
		 * vakantietypeRepository.create(vt2);
		 */
		List<Vakantietype> typeskinderkamp = vakantietypeRepository.findVakantietypeForNaam("KINDERKAMP");
		List<Vakantietype> typeswonen = vakantietypeRepository.findVakantietypeForNaam("BEGELEIDWONENBRUSSEL");

		if (typeskinderkamp != null) {
			vt1 = typeskinderkamp.get(0);
		}
		if (typeswonen != null) {
			vt2 = typeswonen.get(0);
		}

		vakantieProjectRepository.create(vp1);
		vakantieProjectRepository.create(vp2);
		vakantieProjectRepository.create(vp3);
		vakantieProjectRepository.create(vp4);
		vakantieProjectRepository.create(vp5);
		vakantieProjectRepository.create(vp6);
		vp7.setBeginDatum(bd1);
		vp7.setEindDatum(null);
		vp7.setVakantietype(vt1);
		vakantieProjectRepository.create(vp7);
		vp8.setBeginDatum(null);
		vp8.setEindDatum(ed2);
		vp8.setVakantietype(vt2);
		vakantieProjectRepository.create(vp8);
	}

	@Test
	public void getAllVakantieProjectenTest() {
		List<VakantieProject> result = vakantieProjectRepository.findAll();
		assertTrue(result.contains(vp1));
		assertTrue(result.contains(vp2));
		assertTrue(result.contains(vp3));
		assertTrue(result.contains(vp4));
		assertTrue(result.contains(vp5));
		assertTrue(result.contains(vp6));
		assertTrue(result.contains(vp7));
		assertTrue(result.contains(vp8));
		// this is not what we expect, because, apparantly : import.sql has been
		// executed.
		// assertTrue(result.size() == 12);
	}

	@Test
	public void getfindVakantieProjectenDate1Test() {
		List<VakantieProject> result = vakantieProjectRepository.findVakantieProjectWithBegindateEinddateVakantietype(vp7);
		assertTrue(result.contains(vp7));
	}

	@Test
	public void getfindVakantieProjectenDate2Test() {
		List<VakantieProject> result = vakantieProjectRepository.findVakantieProjectWithBegindateEinddateVakantietype(vp7);
		assertFalse(result.contains(vp8));
	}

	@Test
	public void getfindVakantieProjectenTypeTest() {
		List<VakantieProject> result = vakantieProjectRepository.findVakantieProjectWithVakantietype(vp7);
		assertTrue(result.contains(vp7));
	}

	@Test
	public void getfindVakantieProjectenTypeTest2() {
		List<VakantieProject> result = vakantieProjectRepository.findVakantieProjectWithVakantietype(vp8);
		assertTrue(result.contains(vp8));
	}

	@Test
	public void getfindVakantieProjectenTypeTest3() {
		List<VakantieProject> result = vakantieProjectRepository.findVakantieProjectWithVakantietype(vp7);
		assertFalse(result.contains(vp8));
	}
}
