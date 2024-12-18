package club.management.club.shared.Constants;

public class ValidationConstants {
    public static final String CLUB_NOT_FOUND = "Le club n'a pas été trouvé.";
    public static final String IMAGE_EXTENSION_IS_NOT_SUPPORTED = "L'extension de l'image n'est pas prise en charge.";
    public static final String IMAGE_SIZE_SHOULD_BE_UNDER_TWO_MB = "La taille de l'image doit être inférieure à deux Mo.";
    public static final String IMAGE_NOT_FOUND = "L'image n'existe pas.";
    public static final String IMAGE_NOT_VALID = "Le fichier fourni n'est pas une image valide.";
    public static final String TYPE_OBJET_NOT_FOUND = "Type d'objet non supporté.";
    public static final String YOUR_IMAGE_CONTIENT_PROBLEME = "votre image  n'est pas validé.";

    public static final String STUDENT_NOT_FOUND = "L'étudiant spécifié n'existe pas." ;
    public  static  final String INTEGRATION_NOT_FOUND = "Integrarion de cette etudiant n'existe pas .";
    public static final String ROLE_NAME_IS_MANDATORY = "La fonction est obligatoire.";
    public static final String MEMBER_ROLE_IS_MANDATORY = "Le rôle du membre est obligatoire.";
    public static final String ROLE_NAME_IS_NOT_VALID = "Le rôle fourni n'est pas valide.";
    public static final String NOT_AUTHORIZED_TO_DELETE_INTEGRATION = "Vous n'êtes pas autorisé à supprimer cette intégration car elle vous appartient.";
    public static final String NOT_AUTHORIZED_TO_DELETE_ADMINISTRATEUR = "Vous ne pouvez pas supprimer une intégration d'administrateur.";

    public static final String USER_NOT_FOUND = "L'utilisateur n'est pas connecté pour le moment.";
    public static final String UN_SEUL_ADMIN_AUTORISE = "Il ne peut y avoir qu'un seul administrateur dans ce club. Vous ne pouvez pas modifier ce rôle.";

    public static final String PUBLICATION_NOT_FOUND = "Publication not found";
    public static final String COMMENT_NOT_EXIST = "User not authorized to delete this comment or comment does not exist";
    public static final String UNAUTHORIZED_ACTION = "Vous n'êtes pas autorisé à effectuer cette action.";
    // Validation des étudiants
    public static final String EMAIL_FORMAT_INVALID = "Le format de l'email est invalide.";
    public static final String EMAIL_ALREADY_EXISTS = "L'email est déjà utilisé.";
    public static final String FIRST_NAME_IS_MANDATORY = "Le prénom est obligatoire.";
    public static final String LAST_NAME_IS_MANDATORY = "Le nom de famille est obligatoire.";
    public static final String FILIERE_IS_MANDATORY = "La filière est obligatoire.";
    public static final String PASSWORD_IS_MANDATORY = "Le mot de passe est obligatoire.";
    public static final String PASSWORD_FORMAT_INVALID = "Le mot de passe doit contenir au moins 8 caractères, inclure au moins une lettre (majuscule ou minuscule), un chiffre, et un caractère spécial (par exemple, !@#$%^&*).";
    public static final String NOT_AUTHORIZED = "Vous n'êtes pas autorisé à effectuer cette action.";
    public static final String CIN_ALREADY_EXISTS = "Le CIN est déjà utilisé.";
    public static final String CNE_ALREADY_EXISTS = "Le CNE est déjà utilisé.";
    public static final String INVALID_CSV_FILE = "Le fichier téléchargé est invalide.";
    public static final String INVALID_CSV_EXTENSION = "Le fichier téléchargé doit avoir l'extension .csv.";
    public static final String INVALID_CSV_MIME_TYPE = "Le fichier téléchargé n'est pas un fichier CSV valide.";}