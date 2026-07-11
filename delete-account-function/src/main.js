//v3
import { Client, Databases, Storage, Users, Query } from 'node-appwrite';
export default async ({ req, res, log, error}) => {
    const userId = req.headers['x-appwrite-user-id'];

    if (!userId) {
        return res.json({ error: 'User ID not provided' }, 401);
    }
    try {
        const client = new Client() 
            .setEndpoint(process.env.APPWRITE_FUNCTION_API_ENDPOINT)
            .setProject(process.env.APPWRITE_FUNCTION_PROJECT_ID)
            .setKey(req.headers['x-appwrite-key']);

        const databases = new Databases(client);
        const storage = new Storage(client);
        const users = new Users(client);

        const submissionResult = await databases.listDocuments(
            'fitness-coach-db',
            'submission',
            [Query.equal('userId', userId)]
        );

        const workoutsResult = await databases.listDocuments(
            'fitness-coach-db',
            'workouts',
            [Query.equal('userId', userId)]
        );

        const setsResult = await databases.listDocuments(
            'fitness-coach-db',
            'sets',
            [Query.equal('userId', userId)]
        );

        const scheduleResult = await databases.listDocuments(
            'fitness-coach-db',
            'schedule',
            [Query.equal('userId', userId)]
        );

        const userSettings = await databases.getDocument('fitness-coach-db', 'user_settings', userId);

        const pendingSubmissions = submissionResult.documents.filter(doc => doc.status === 'pending');

        for (const schedule of scheduleResult.documents) {
            await databases.deleteDocument('fitness-coach-db', 'schedule', schedule.$id);
        }

        for (const set of setsResult.documents) {
            await databases.deleteDocument('fitness-coach-db', 'sets', set.$id);
        }

        for (const workout of workoutsResult.documents) {
            await databases.deleteDocument('fitness-coach-db', 'workouts', workout.$id);
        }

        for (const submission of pendingSubmissions) {
            await databases.deleteDocument('fitness-coach-db', 'submission', submission.$id);
            await storage.deleteFile('submission-videos', submission.videoFileId);
        }
        await databases.deleteDocument('fitness-coach-db', 'user_settings', userId);
        if(userSettings.profileIconId) {
            await storage.deleteFile('6a4f6a49000bb69ef75a', userSettings.profileIconId);
        }
        await users.delete(userId);

        return res.json({ success: true});
    } catch (err) {
        error(err);
        return res.json({ error: 'Failed to delete account' }, 500);
    }
};