package com.gilson.agromarket.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseConfig {

    private static DatabaseReference reference;
    private static FirebaseAuth firebaseAuth;
    private static StorageReference storageReference;
    private static volatile boolean isPersistenceEnable = false;

    //retornar uma instancia do firebase
    public static FirebaseAuth getFirebaseAuth(){

        if (firebaseAuth == null){

            firebaseAuth = FirebaseAuth.getInstance();

        }

        return firebaseAuth;

    }

    //retornar uma referencia do database
    public static DatabaseReference getReference(){

        if (reference == null){

            reference = FirebaseDatabase.getInstance().getReference();

        }

        return reference;
    }

    public static StorageReference getStorageReference(){

        if (storageReference == null){
            storageReference = FirebaseStorage.getInstance().getReference();
        }

        return storageReference;

    }

    public static void enableDatabasePersistence(){
        if (!isPersistenceEnable){
            synchronized (FirebaseConfig.class){
                if (!isPersistenceEnable){
                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                    isPersistenceEnable = true;
                }
            }
        }
    }


}
