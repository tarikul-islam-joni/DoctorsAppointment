package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;

public class DataModel
{
    public String[] GetPatientAccountInformationDataKey()
    {
        return new String[]{DBConst.ProfileImageUrl,DBConst.Name,DBConst.FatherName,DBConst.MotherName,
                DBConst.PhoneNumber,DBConst.Gender,DBConst.DateOfBirth,DBConst.BloodGroup,DBConst.Address,
                DBConst.BirthCertificateNumber,DBConst.BirthCertificateImageUrl,DBConst.AnotherDocumentImageUrl};
    }
    public String[] GetDoctorAccountInformationDataKey()
    {
        return new String[]{DBConst.ProfileImageUrl,DBConst.Name,DBConst.StudiedCollege,DBConst.PassingYear,
                DBConst.CompletedDegree,DBConst.NoOfPracticingYear,DBConst.PhoneNumber,DBConst.AvailableArea,
                DBConst.Specialization, DBConst.NIDNumber,DBConst.BMDCRegNumber,
                DBConst.NIDImageUrl,DBConst.BMDCRegImageUrl, DBConst.AuthorityValidity};
    }
}
