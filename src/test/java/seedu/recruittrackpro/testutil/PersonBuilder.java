package seedu.recruittrackpro.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.recruittrackpro.model.person.Address;
import seedu.recruittrackpro.model.person.Comment;
import seedu.recruittrackpro.model.person.Email;
import seedu.recruittrackpro.model.person.Name;
import seedu.recruittrackpro.model.person.Person;
import seedu.recruittrackpro.model.person.Phone;
import seedu.recruittrackpro.model.tag.Tag;
import seedu.recruittrackpro.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_COMMENT = "";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;
    private Comment comment;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
        comment = new Comment(DEFAULT_COMMENT);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        tags = new HashSet<>(personToCopy.getTags());
        comment = personToCopy.getComment();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Adds the specified {@code tags} to the existing set of tags for the {@code Person} being built,
     * preserving previously added tags.
     */
    public PersonBuilder addTags(String ... tags) {
        this.tags.addAll(SampleDataUtil.getTagSet(tags));
        return this;
    }


    /**
     * Removes a specified tag from the person's tag list.
     *
     * @param tag The tag to be removed.
     * @return The updated PersonBuilder instance.
     */
    public PersonBuilder removeTags(String tag) {
        this.tags.remove(new Tag(tag));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Comment} of the {@code Person} that we are building.
     */
    public PersonBuilder withComment(String comment) {
        this.comment = new Comment(comment);
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, address, tags, comment);
    }

}
