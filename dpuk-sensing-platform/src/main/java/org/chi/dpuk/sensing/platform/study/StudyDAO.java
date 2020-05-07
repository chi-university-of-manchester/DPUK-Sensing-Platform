package org.chi.dpuk.sensing.platform.study;

import java.util.List;

import org.chi.dpuk.sensing.platform.model.Study;

public interface StudyDAO {

	public Study addOrUpdateStudy(Study study);

	public Study getStudy(String name);

	public Study getStudy(long studyId);

	public void deleteStudy(long studyId);

	public List<Study> getAllStudies();

}