package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

public class DBConst
{
    ///RESULT
    public static final String UNKNOWN="UNKNOWN";
    public static final String GetAccountUID="GetAccountUID";
    public static final String RESULT="RESULT";
    public static final String NULL_USER="NULL_USER";
    public static final String NOT_NULL_USER="NOT_NULL_USER";
    public static final String DATA_EXIST="DATA_EXIST";
    public static final String DATA_NOT_EXIST="DATA_NOT_EXIST";
    public static final String DATABASE_ERROR="DATABASE_ERROR";
    public static final String SUCCESSFUL="SUCCESSFUL";
    public static final String UNSUCCESSFUL="UNSUCCESSFUL";
    public static final String GetStatusOnAccountDeletion="GetStatusOnAccountDeletion";
    public static final String GetStatusOnSendingForgetPasswordLinkToEmail="GetStatusOnSendingForgetPasswordLinkToEmail";

    //ACCOUNT NODE
    public static final String Account="Account";
    public static final String Admin="Admin";
    public static final String Patient="Patient";
    public static final String Doctor="Doctor";
    public static final String PatientAccount="PatientAccount";
    public static final String DoctorAccount="DoctorAccount";

    public static final String AccountInformation="AccountInformation";
    public static final String SavePatientAccountInformation="SaveAccountInformation";
    public static final String GetPatientAccountInformation="GetAccountInformation";

    public static final String ProfileImageUrl="ProfileImageUrl";
    public static final String Name="Name";

    public static final String FatherName="FatherName";
    public static final String MotherName="MotherName";
    public static final String PhoneNumber="PhoneNumber";
    public static final String Height="Height";
    public static final String Weight="Weight";
    public static final String Gender="Gender";
    public static final String BloodGroup="BloodGroup";
    public static final String DateOfBirth="DateOfBirth";
    public static final String Address="Address";
    public static final String BirthCertificateNumber="BirthCertificateNumber";
    public static final String BirthCertificateImageUrl="BirthCertificateImageUrl";
    public static final String AnotherDocumentImageUrl="AnotherDocumentImageUrl";

    public static final String GetDoctorAccountInformation="GetDoctorAccountInformation";
    public static final String SaveDoctorAccountInformation="SaveDoctorAccountInformation";
    public static final String StudiedCollege="StudiedCollege";
    public static final String PassingYear="PassingYear";
    public static final String CompletedDegree="CompletedDegree";
    public static final String Specialization="Specialization";
    public static final String NoOfPracticingYear="NoOfPracticingYear";
    public static final String AvailableArea="AvailableArea";
    public static final String BMDCRegNumber="BMDCRegNumber";
    public static final String BMDCRegImageUrl="BMDCRegImageUrl";
    public static final String NIDNumber="NIDNumber";
    public static final String NIDImageUrl="NIDImageUrl";
    public static final String AuthorityValidity="AuthorityValidity";
    public static final String UNVERIFIED="UNVERIFIED";
    public static final String VERIFIED="VERIFIED";

    //Account Status DB
    public static final String GetAccountStatusDB="GetAccountStatusDB";
    public static final String SaveAccountStatusDB="SaveAccountStatusDB";
    public static final String AccountStatus="AccountStatus";
    public static final String AccountType="AccountType";
    public static final String AccountCompletion="AccountCompletion";
    public static final String AccountValidity="AccountValidity";
    public static final String AccountLockState="AccountLockState";

    //Account Multiplicity DB
    public static final String GetAccountMultiplicityDB="GetAccountMultiplicityDB";
    public static final String SaveAccountMultiplicityDB="SaveAccountMultiplicityDB";
    public static final String AccountMultiplicity="AccountMultiplicity";
    public static final String MultipleCheck="MultipleCheck";
    public static final String UID="UID";

    //Appointment Schedule
    public static final String AppointmentSchedule="AppointmentSchedule";
    public static final String HospitalName="HospitalName";
    public static final String AvailableDay="AvailableDay";
    public static final String AppointmentSTime="AppointmentSTime";
    public static final String AppointmentETime="AppointmentETime";
    public static final String UnavailableSDate="UnavailableSDate";
    public static final String UnavailableEDate="UnavailableEDate";
    public static final String AppointmentFee="AppointmentFee";
    public static final String AppointmentCapacity="AppointmentCapacity";

    //Hospital Db
    public static final String DoctorFiltration="DoctorFiltration";
    public static final String DoctorList="DoctorList";
    public static final String HospitalList="HospitalList";
    public static final String DivisionList="DivisionList";

    //Storage
    public static final String GetSavingFileUrlData="GetSavingFileUrlData";
    public static final String URL="URL";
    public static final String ProfileImages="ProfileImages";
    public static final String SecureData="SecureData";

    //Temporary Appointment
    public static final String TemporaryAppointment="TemporaryAppointment";
    public static final String AppointmentDate="AppointmentDate";
    public static final String AppointmentValidityTime="AppointmentValidityTime";
    //Confirm Appointment
    public static final String ConfirmAppointment="ConfirmAppointment";
    public static final String AppointmentCreateDate="AppointmentCreateDate";

    public static final String PatientList="PatientList";

    public static final String CurrentTime="CurrentTime";

    public static final String GetStatusOnSave_SS_SP_AA_InDoctorFiltration="GetStatusOnSave_SS_SP_AA_InDoctorFiltration";

    public static final String GetAppointmentScheduleInfo="GetAppointmentScheduleInfo";
    public static final String GetStatusOnSaveAppointmentScheduleInfoInAccount="GetStatusOnSaveAppointmentScheduleInfoInAccount";
    public static final String GetStatusOnDeleteAppointmentScheduleInfoFromAccount="GetStatusOnDeleteAppointmentScheduleInfoFromAccount";
    public static final String GetStatusOnSaveDoctorUIDInHospitalDir="GetStatusOnSaveDoctorUIDInHospitalDir";
    public static final String GetStatusOnDeleteDoctorUIDFromHospitalDir="GetStatusOnDeleteDoctorUIDFromHospitalDir";

    public static final String RatingAndReviews="RatingAndReviews";


    public static final String GetDoctorListByDivision="GetDoctorListByDivision";
    public static final String GetDoctorListByDisease="GetDoctorListByDisease";
    public static final String GetDoctorListByHospital="GetDoctorListByHospital";
    public static final String GetDivisionListWithData="GetDivisionListWithData";
    public static final String GetDiseaseTypeWithData="GetDiseaseTypeWithData";
    public static final String GetHospitalListWithData="GetHospitalListWithData";

    public static final String DataSnapshot="DataSnapshot";


    public static final String GetDoctorFullDatabase="GetDoctorFullDatabase";
    public static final String GetRatingsAndReviewInfo="GetRatingsAndReviewInfo";


    public static final String GetStatusOnSaveReviewAndRatingIntoToDoctorAccountDB="GetStatusOnSaveReviewAndRatingIntoToDoctorAccountDB";
    public static final String GetRatingsAndReviewInfoFromDoctorAccountDB="GetRatingsAndReviewInfoFromDoctorAccountDB";
    public static final String Ratings="Ratings";
    public static final String Reviews="Reviews";
    public static final String TotalRating="TotalRating";
    public static final String TotalReviewer="TotalReviewer";
    public static final String ExpandReview="ExpandReview";


    public static final String AppointmentHistory="AppointmentHistory";
    public static final String GetDoctorSelectedDateAppointmentHistoryFromDoctorAccount="GetDoctorSelectedDateAppointmentHistoryFromDoctorAccount";
    public static final String GetDoctorAppointmentHistoryFromDoctorAccount="GetDoctorAppointmentHistoryFromDoctorAccount";
    public static final String GetPatientAppointmentHistoryFromPatientAccount="GetPatientAppointmentHistoryFromPatientAccount";
    public static final String GetTodayDoctorScheduledAppointmentList="GetTodayDoctorScheduledAppointmentList";
    public static final String GetYesterdayDoctorScheduledAppointmentList="GetYesterdayDoctorScheduledAppointmentList";
    public static final String GetTomorrowDoctorScheduledAppointmentList="GetTomorrowDoctorScheduledAppointmentList";
    public static final String GetCustomDateDoctorScheduledAppointmentList="GetCustomDateDoctorScheduledAppointmentList";
    public static final String GetNextSevenDaysDoctorScheduledAppointmentList="GetNextSevenDaysDoctorScheduledAppointmentList";
    public static final String GetEntireMonthDoctorScheduledAppointmentList="GetEntireMonthDoctorScheduledAppointmentList";
    public static final String GetSelectedMonthWithYearDateAppointmentHistoryFromDoctorAccount="GetSelectedMonthWithYearDateAppointmentHistoryFromDoctorAccount";


    public static final String GetStatusOnSaveAppointmentCreationFromDoctorDB="GetStatusOnSaveAppointmentCreationFromDoctorDB";
    public static final String GetStatusOnSaveAppointmentCreationFromPatientDB="GetStatusOnSaveAppointmentCreationFromPatientDB";
    public static final String SerialNo="SerialNo";
    public static final String AppointmentTime="AppointmentTime";

    public static final String PaymentDB="PaymentDB";
    public static final String ReferenceKey="ReferenceKey";
    public static final String TraxId="TraxId";
    public static final String GetPaymentDataFromPaymentDB="GetPaymentDataFromPaymentDB";

    public static final String SELF="SELF";


    public static final String MedicalReportDB="MedicalReportDB";
    public static final String ReportTitle="ReportTitle";
    public static final String ReportDetails="ReportDetails";
    public static final String GetStatusOnDeleteSingleImageFromPatientAccount="GetStatusOnDeleteSingleImageFromPatientAccount";
    public static final String GetStatusOnDeleteSingleImageFromStorageDB="GetStatusOnDeleteSingleImageFromStorageDB";
    public static final String GetStatusOnDeleteEntireReportFromPatientAccount="GetStatusOnDeleteEntireReportFromPatientAccount";
    public static final String GetStatusOnDeleteEntireReportFromStorageDB="GetStatusOnDeleteEntireReportFromStorageDB";
    public static final String GetStatusOnSaveReportDetailsIntoPatientAccountDB="GetStatusOnSaveReportDetailsIntoPatientAccountDB";
    public static final String GetStatusOnSaveReportImageToTheStorageDB="GetStatusOnSaveReportImageToTheStorageDB";
    public static final String GetReportDetailsFromPatientAccountDB="GetReportDetailsFromPatientAccountDB";

    public static final String MonthWithYear="MonthWithYear";
    public static final String OnlyYear="OnlyYear";


    public static final String GetBillForTheSelectedDateFromDB="GetBillForTheSelectedDateFromDB";
    public static final String GetBillForTheSelectedMonthWithYearFromDB="GetBillForTheSelectedMonthWithYearFromDB";


    public static final String BackActivity="BackActivity";


    public static final String GetSpecificDoctorBMDCInfo="GetSpecificDoctorBMDCInfo";
    public static final String GetAccountMultiplicityDoctorAccountListFromDB="GetAccountMultiplicityDoctorAccountListFromDB";
    public static final String GetAccountMultiplicityPatientAccountListFromDB="GetAccountMultiplicityPatientAccountListFromDB";
    public static final String GetLockedDoctorAccountListFromDB="GetLockedDoctorAccountListFromDB";
    public static final String GetLockedPatientAccountListFromDB="GetLockedPatientAccountListFromDB";
}
