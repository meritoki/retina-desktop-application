from panoptes_client import Panoptes, Project, SubjectSet, Subject

Panoptes.connect(username='ACRE_AR', password='dedalo2018')

display_name="Test Project 4"

for project in Project.where():
    print(project.title)
    # if project.title == display_name
    #     print(display_name)

# new_project = Project()
# new_project.display_name = 'Test Project 4'
# new_project.description = 'A great new project!'
# new_project.primary_language = 'en'
# new_project.private = True
# new_project.save()
#
# subject_set = SubjectSet()
#
# subject_set.links.project = new_project
# subject_set.display_name = 'Tutorial subject set'
# subject_metadata = {
#     './01.jpg': {
#         'subject_reference': 1,
#         'date': '2017-01-01',
#     }
# }
#
# new_subjects = []
#
# for filename, metadata in subject_metadata.items():
#     subject = Subject()
#
#     subject.links.project = new_project
#     subject.add_location(filename)
#
#     subject.metadata.update(metadata)
#
#     subject.save()
#     new_subjects.append(subject)
# subject_set.add(new_subjects)
# subject_set.save()
