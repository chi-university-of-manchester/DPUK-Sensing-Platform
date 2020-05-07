package org.chi.dpuk.sensing.platform.config;

import java.util.Date;

import org.chi.dpuk.sensing.platform.dataSet.DataSetService;
import org.chi.dpuk.sensing.platform.dataSource.DataSourceService;
import org.chi.dpuk.sensing.platform.model.DataSet;
import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.model.User;
import org.chi.dpuk.sensing.platform.model.User.Role;
import org.chi.dpuk.sensing.platform.participant.ParticipantService;
import org.chi.dpuk.sensing.platform.security.HashingUtility;
import org.chi.dpuk.sensing.platform.study.StudyService;
import org.chi.dpuk.sensing.platform.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Used to configure the system and add test data.
 */

@Service
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {

	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Autowired
	private StudyService studyService;

	@Autowired
	private UserService userService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private DataSetService dataSetService;

	@Override
	public void configureDatabase() {
		log.info("Configuring database...");
		try {
			// Load any initial data into the database.
			addTestData();
		} catch (Exception e) {
			log.error("Problem configuring database.", e);
		}
		log.info("Done configuring database.");
	}

	@Override
	public void addTestData() {
		// A few users
		if (userService.getUser("daniel") == null) {
			User u1 = new User();
			u1.setUserName("daniel");
			String hashedPassword = HashingUtility.hashPassword("admin123");
			u1.setPasswordHash(hashedPassword);
			u1.setEmail("daniel.cave@manchester.ac.uk");
			u1.setRole(Role.ROLE_RESEARCHER);
			u1.setTitle("Mr");
			u1.setFirstName("Daniel");
			u1.setLastName("Cave");
			userService.saveUser(u1);
		}

		if (userService.getUser("rajesh") == null) {
			User u2 = new User();
			u2.setUserName("rajesh");
			String hashedPassword = HashingUtility.hashPassword("admin123");
			u2.setPasswordHash(hashedPassword);
			u2.setEmail("rajesh.bhadra@manchester.ac.uk");
			u2.setRole(Role.ROLE_RESEARCHER);
			u2.setTitle("Mr");
			u2.setFirstName("Rajesh");
			u2.setLastName("Bhadra");
			userService.saveUser(u2);
		}

		if (userService.getUser("researcher") == null) {
			User u3 = new User();
			u3.setUserName("researcher");
			String hashedPassword = HashingUtility.hashPassword("admin123");
			u3.setPasswordHash(hashedPassword);
			u3.setEmail("daniel.cave@manchester.ac.uk");
			u3.setRole(Role.ROLE_RESEARCHER);
			u3.setTitle("Mr");
			u3.setFirstName("Researcher");
			u3.setLastName("User");
			userService.saveUser(u3);
		}

		// Create the test study user if needed.
		if (studyService.getStudy("Test Study 1") == null) {
			Study s1 = new Study();
			s1.setName("Test Study 1");
			s1.setDescription("Test description 1.");
			studyService.addOrUpdateStudy(s1);
		}

		if (studyService.getStudy("Test Study 2") == null) {
			Study s2 = new Study();
			s2.setName("Test Study 2");
			s2.setDescription("Test description 2.");
			studyService.addOrUpdateStudy(s2);
		}

		// Assign user u1 to study s1
		studyService.addUserToStudy(userService.getUser("daniel").getId(),
				studyService.getStudy("Test Study 1").getId());

		// Assign user u2 to study s2
		studyService.addUserToStudy(userService.getUser("rajesh").getId(),
				studyService.getStudy("Test Study 2").getId());

		// Assign study s2 to user u3
		userService.addStudyToUser(studyService.getStudy("Test Study 2").getId(),
				userService.getUser("researcher").getId());

		// Create participants.
		Participant participant1 = new Participant();
		participant1.setName("Participant Name 1");
		participant1.setAddress("Participant Address 1");
		participant1.setContactNumber("01134567890");
		participant1.setNhsNumber("2046836948");
		participantService.addOrUpdateParticipant(participant1);

		Participant participant2 = new Participant();
		participant2.setName("Participant Name 1");
		participant2.setAddress("Participant Address 2");
		participant2.setContactNumber("01617865467");
		participant2.setNhsNumber("3069246856");
		participantService.addOrUpdateParticipant(participant2);

		Participant participant3 = new Participant();
		participant3.setName("Participant Name 2");
		participant3.setAddress("Participant Address 3");
		participant3.setContactNumber("0783893090303");
		participant3.setNhsNumber("1234567890");
		participantService.addOrUpdateParticipant(participant3);

		Participant participant4 = new Participant();
		participant4.setName("Participant Name 2");
		participant4.setAddress("Participant Address 4");
		participant4.setContactNumber("080789020291");
		participant4.setNhsNumber("9999999999");
		participantService.addOrUpdateParticipant(participant4);

		// Assign participant participant1 to study s1
		studyService.addParticipantToStudy(participantService.getParticipants("Participant Name 1").get(0).getId(),
				studyService.getStudy("Test Study 1").getId());

		// Assign participant participant2 to study s1
		studyService.addParticipantToStudy(participantService.getParticipants("Participant Name 1").get(1).getId(),
				studyService.getStudy("Test Study 1").getId());

		// Assign study s2 to participant participant3
		participantService.addStudyToParticipant(studyService.getStudy("Test Study 2").getId(),
				participantService.getParticipants("Participant Name 2").get(0).getId());

		// Assign study s2 to participant participant4
		participantService.addStudyToParticipant(studyService.getStudy("Test Study 2").getId(),
				participantService.getParticipants("Participant Name 2").get(1).getId());

		// Create dataSources.
		DataSource dataSource1 = new DataSource();
		dataSource1.setName("Test DataSource 1");
		dataSource1.setDescription("Description 1");
		dataSource1.setType("Type 1");
		dataSource1.setUserName("UserName1");
		dataSource1.setPassword("Password 1");
		encryptPassword(dataSource1);
		dataSourceService.addOrUpdateDataSource(dataSource1);

		DataSource dataSource2 = new DataSource();
		dataSource2.setName("Test DataSource 2");
		dataSource2.setDescription("Description 2");
		dataSource2.setType("Type 2");
		dataSource2.setUserName("UserName2");
		dataSource2.setPassword("Password 2");
		encryptPassword(dataSource2);
		dataSourceService.addOrUpdateDataSource(dataSource2);

		DataSource dataSource3 = new DataSource();
		dataSource3.setName("Test DataSource 3");
		dataSource3.setDescription("Description 3");
		dataSource3.setType("Type 3");
		dataSource3.setUserName("UserName3");
		dataSource3.setPassword("Password 3");
		encryptPassword(dataSource3);
		dataSourceService.addOrUpdateDataSource(dataSource3);

		DataSource dataSource4 = new DataSource();
		dataSource4.setName("Test DataSource 4");
		dataSource4.setDescription("Description 4");
		dataSource4.setType("Type 4");
		dataSource4.setUserName("UserName4");
		dataSource4.setPassword("Password 4");
		encryptPassword(dataSource4);
		dataSourceService.addOrUpdateDataSource(dataSource4);

		// Assign dataSource dataSource1 to participant participant1
		dataSourceService.addParticipantToDataSource(
				participantService.getParticipants("Participant Name 1").get(0).getId(),
				dataSourceService.getAllDataSources().get(0).getId());

		// Assign dataSource dataSource2 to participant participant2
		dataSourceService.addParticipantToDataSource(
				participantService.getParticipants("Participant Name 1").get(1).getId(),
				dataSourceService.getAllDataSources().get(1).getId());

		// Assign participant participant3 to dataSource dataSource3
		participantService.addDataSourceToParticipant(dataSourceService.getAllDataSources().get(2).getId(),
				participantService.getParticipants("Participant Name 2").get(0).getId());

		// Assign participant participant4 to dataSource dataSource4
		participantService.addDataSourceToParticipant(dataSourceService.getAllDataSources().get(3).getId(),
				participantService.getParticipants("Participant Name 2").get(1).getId());

		// Create dataSets.
		DataSet dataSet1 = new DataSet();
		dataSet1.setStartDate(new Date());
		dataSet1.setEndDate(new Date());
		dataSet1.setPayload("Pay Load 1".getBytes());
		dataSet1.setPayloadContentType("Payload Content Type 1");
		dataSet1.setPayloadNameWithExtension("Payload Name With Extension 1");
		dataSetService.addDataSet(dataSet1);

		// Assign dataSet dataSet1 to dataSource dataSource1
		dataSourceService.addDataSetToDataSource(dataSet1, dataSourceService.getAllDataSources().get(0).getId());

		DataSet dataSet2 = new DataSet();
		dataSet2.setStartDate(new Date());
		dataSet2.setEndDate(new Date());
		dataSet2.setPayload("Pay Load 2".getBytes());
		dataSet2.setPayloadContentType("Payload Content Type 2");
		dataSet2.setPayloadNameWithExtension("Payload Name With Extension 2");
		dataSetService.addDataSet(dataSet2);

		// Assign dataSet dataSet2 to dataSource dataSource2
		dataSourceService.addDataSetToDataSource(dataSet2, dataSourceService.getAllDataSources().get(1).getId());

		DataSet dataSet3 = new DataSet();
		dataSet3.setStartDate(new Date());
		dataSet3.setEndDate(new Date());
		dataSet3.setPayload("Pay Load 3".getBytes());
		dataSet3.setPayloadContentType("Payload Content Type 3");
		dataSet3.setPayloadNameWithExtension("Payload Name With Extension 3");
		dataSetService.addDataSet(dataSet3);

		// Assign dataSet dataSet3 to dataSource dataSource3
		dataSourceService.addDataSetToDataSource(dataSet3, dataSourceService.getAllDataSources().get(2).getId());

		DataSet dataSet4 = new DataSet();
		dataSet4.setStartDate(new Date());
		dataSet4.setEndDate(new Date());
		dataSet4.setPayload("Pay Load 4".getBytes());
		dataSet4.setPayloadContentType("Payload Content Type 4");
		dataSet4.setPayloadNameWithExtension("Payload Name With Extension 4");
		dataSetService.addDataSet(dataSet4);

		// Assign dataSet dataSet4 to dataSource dataSource4
		dataSourceService.addDataSetToDataSource(dataSet4, dataSourceService.getAllDataSources().get(3).getId());
	}

	private void encryptPassword(DataSource dataSource) {
		String password = dataSource.getPassword();
		if (password != null) {
			dataSource.setPassword(HashingUtility.hashPassword(dataSource.getPassword()));
		}
	}

}