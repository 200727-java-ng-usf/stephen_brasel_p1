package com.revature.services;

import com.revature.daos.ReimbursementDao;
import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ReimbursementServiceTest {
	private ReimbursementService sut;
	private ReimbursementDao mopitory;
	private AppUser mocauthor;
	private AppUser mocolver;
	private Reimbursement reimb1;
	private Reimbursement reimb2;
	private Reimbursement reimb3;
	private Reimbursement reimb4;
	private Reimbursement reimb5;
	private long count = 1;
	private List<Reimbursement> mockReimbs;

//	mockUsers.add(new AppUser(1, "Alice", "Anderson", "aanderson", "password", "aanderson@revature.net",Role.ADMIN));


	@Before
	public void setUp() throws Exception {
		mopitory = Mockito.mock(ReimbursementDao.class);
		sut = new ReimbursementService();
		sut.setReimbursementDao(mopitory);
		mocauthor = Mockito.mock(AppUser.class);
		mocolver = Mockito.mock(AppUser.class);
		count = 1;
		reimb1 = setupReimbursement(reimb1);
		reimb2 = setupReimbursement(reimb2);
		reimb3 = setupReimbursement(reimb3);
		reimb4 = setupReimbursement(reimb4);
		reimb5 = setupReimbursement(reimb5);
		mockReimbs = new ArrayList<>();
		mockReimbs.add(reimb1);
		mockReimbs.add(reimb2);
		mockReimbs.add(reimb3);
		mockReimbs.add(reimb4);
		mockReimbs.add(reimb5);
//		mockReimbs.add();
	}
	private Reimbursement setupReimbursement(Reimbursement r){
		r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn((int) count++);
		when(r.getAmount()).thenReturn(20.00d * count);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - (10000 * count))));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("Da ba dee, da a die ");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		return r;
	}

	@After
	public void tearDown() throws Exception {
		sut = null;
		mopitory = null;
		mocauthor = null;
		mocolver = null;
		reimb1 = null;
		reimb2 = null;
		reimb3 = null;
		reimb4 = null;
		reimb5 = null;
		count = 1;
		mockReimbs = null;
	}

	@Test
	public void nullCheck() {
		assertNotNull(sut);
	}

	@Test
	public void getReimbursementDao() {
		assertEquals(mopitory, sut.getReimbursementDao());
	}

	@Test
	public void setReimbursementDao() {
		assertEquals(mopitory, sut.getReimbursementDao());
	}

	@Test
	public void getAllReimbursements() {
		when(mopitory.findAll()).thenReturn(mockReimbs);
		try {
			assertArrayEquals(mockReimbs.toArray(), sut.getAllReimbursements().toArray());
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test(expected = ResourceNotFoundException.class)
	public void getAllReimbursementsNone() throws ResourceNotFoundException{
		sut.getAllReimbursements();
	}

	@Test(expected = ResourceNotFoundException.class)
	public void getReimbursementById() throws ResourceNotFoundException {
		assertNull(sut.getReimbursementById(1));
	}

	@Test(expected = InvalidRequestException.class)
	public void getReimbursementByInvalidId() throws InvalidRequestException {
		try {
			assertNull(sut.getReimbursementById(-1));
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getReimbursementByAuthor() throws ResourceNotFoundException {
		mockReimbs = new ArrayList<>();
		mockReimbs.add(reimb1);
		when(mopitory.findByAuthor(1)).thenReturn(mockReimbs);
		assertArrayEquals(mockReimbs.toArray(), sut.getReimbursementByAuthor(1).toArray());
	}

	@Test(expected = InvalidRequestException.class)
	public void getReimbursementByInvalidAuthor() throws InvalidRequestException {
		when(mocauthor.getId()).thenReturn(-1);
		try {
			sut.getReimbursementByAuthor(mocauthor.getId());
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test(expected = ResourceNotFoundException.class)
	public void getReimbursementByUnknownAuthor() throws ResourceNotFoundException {
		when(mocauthor.getId()).thenReturn(1);
		sut.getReimbursementByAuthor(mocauthor.getId());
	}

	@Test
	public void getPendingReimbursementByAuthor() throws ResourceNotFoundException {
		mockReimbs = new ArrayList<>();
		mockReimbs.add(reimb1);
		when(mopitory.findPendingReimbursementsByAuthor(1)).thenReturn(mockReimbs);
		assertArrayEquals(mockReimbs.toArray(), sut.getPendingReimbursementByAuthor(1).toArray());
	}

	@Test(expected = InvalidRequestException.class)
	public void getPendingReimbursementByInvalidAuthor() throws InvalidRequestException {
		when(mocauthor.getId()).thenReturn(-1);
		try {
			sut.getPendingReimbursementByAuthor(mocauthor.getId());
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test(expected = ResourceNotFoundException.class)
	public void getPendingReimbursementByUnknownAuthor() throws ResourceNotFoundException {
		when(mocauthor.getId()).thenReturn(1);
		sut.getPendingReimbursementByAuthor(mocauthor.getId());
	}

	//TODO
	@Test
	public void submit() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		sut.submit(r);
	}

	//TODO
	@Test(expected = InvalidRequestException.class)
	public void submitInvalid() throws InvalidRequestException{
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(-20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		sut.submit(r);
	}

	//TODO
	@Test
	public void updateReimbursement() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		when(mopitory.update(r)).thenReturn(true);
		assertTrue(sut.updateReimbursement(r));
	}

	//TODO
	@Test(expected = InvalidRequestException.class)
	public void updateInvalidReimbursement() throws InvalidRequestException{
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(-20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		when(mopitory.update(r)).thenReturn(true);
		sut.updateReimbursement(r);
	}

	//TODO
	@Test
	public void deleteReimbursementById() {
		when(mopitory.deleteById(1)).thenReturn(true);
		assertTrue(sut.deleteReimbursementById(1));
	}

	//TODO
	@Test(expected = InvalidRequestException.class)
	public void deleteReimbursementByNegativeId() throws InvalidRequestException {
		when(mopitory.deleteById(1)).thenReturn(true);
		sut.deleteReimbursementById(-1);
	}

	//TODO ALL CASES.
	@Test
	public void isReimbursementValid() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		assertTrue(sut.isReimbursementValid(r));
	}

	//TODO ALL CASES.
	@Test
	public void isNullReimbursementValid() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		assertFalse(sut.isReimbursementValid(null));
	}

	//TODO ALL CASES.
	@Test
	public void isNegativeAmountReimbursementValid() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(-20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		assertFalse(sut.isReimbursementValid(r));
	}

	//TODO ALL CASES.
	@Test
	public void isNullDescriptionReimbursementValid() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn(null);
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		assertFalse(sut.isReimbursementValid(r));
	}

	//TODO ALL CASES.
	@Test
	public void isEmptyDescriptionReimbursementValid() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		assertFalse(sut.isReimbursementValid(r));
	}

	//TODO ALL CASES.
	@Test
	public void isEmptySpaceyDescriptionReimbursementValid() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("                  ");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		assertFalse(sut.isReimbursementValid(r));
	}

	//TODO ALL CASES.
	@Test
	public void isNullSubmissionReimbursementValid() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn(null);
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		assertFalse(sut.isReimbursementValid(r));
	}

	//TODO ALL CASES.
	@Test
	public void isOldSubmissionReimbursementValid() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(1597648962389L - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		assertFalse(sut.isReimbursementValid(r));
	}

	//TODO ALL CASES.
	@Test
	public void isTimeParadoxSubmissionReimbursementValid() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn((new Timestamp(System.currentTimeMillis() - 80000)));
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		assertFalse(sut.isReimbursementValid(r));
	}

	//TODO ALL CASES.
	@Test
	public void isNullAuthorReimbursementValid() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(null);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		assertFalse(sut.isReimbursementValid(r));
	}

	//TODO ALL CASES.
	@Test
	public void isNullStatusReimbursementValid() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(null);
		when(r.getReimb_type_id()).thenReturn(ReimbursementType.TRAVEL);
		assertFalse(sut.isReimbursementValid(r));
	}

	//TODO ALL CASES.
	@Test
	public void isNullTypeReimbursementValid() {
		Reimbursement r = Mockito.mock(Reimbursement.class);
		when(r.getId()).thenReturn(1);
		when(r.getAmount()).thenReturn(20.00d);
		when(r.getSubmitted()).thenReturn((new Timestamp(System.currentTimeMillis() - 50000)));
		when(r.getResolved()).thenReturn(null);
		when(r.getDescription()).thenReturn("I'm blue");
		when(r.getReceiptURI()).thenReturn("");
		when(r.getAuthor()).thenReturn(mocauthor);
		when(r.getResolver()).thenReturn(mocolver);
		when(r.getReimb_status_id()).thenReturn(ReimbursementStatus.PENDING);
		when(r.getReimb_type_id()).thenReturn(null);
		assertFalse(sut.isReimbursementValid(r));
	}
}