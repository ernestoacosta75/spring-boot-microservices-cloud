package com.thoughtmechanix.licenses.services;

import com.thoughtmechanix.licenses.model.License;

import java.util.List;

public interface LicenseService {

    public License getLicense(String organizationId, String licenseId);

    public List<License> getLicensesByOrg(String organizationId);

    public License getLicensesWithClient(String organizationId, String licenseId, String clientType);

    public void saveLicense(License license);

    public void updateLicense(License license);

    public void deleteLicense(License license);


}
