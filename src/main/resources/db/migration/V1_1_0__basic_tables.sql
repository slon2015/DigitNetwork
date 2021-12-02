CREATE TABLE "Profile"(
    "owner_name" VARCHAR NOT NULL PRIMARY KEY,
    "nickname" VARCHAR NOT NULL,
    "status_text" VARCHAR
);

CREATE TABLE "Post"(
    "id" VARCHAR NOT NULL PRIMARY KEY,
    "profile_id" VARCHAR NOT NULL,
    "post_text" VARCHAR NOT NULL,
    FOREIGN KEY ("profile_id") REFERENCES "Profile"("owner_name")
);